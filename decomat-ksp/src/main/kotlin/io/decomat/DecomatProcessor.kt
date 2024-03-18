package io.decomat

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*


class DecomatProcessor(
  private val logger: KSPLogger,
  val codeGenerator: CodeGenerator,
  val matchableAnnotationName: String,
  val componentAnnotationName: String,
  val middleComponentAnnotationName: String,
  val constructorComponentAnnotationName: String,
  val renderAdtFunctions: Boolean,
  val fromHereFunctionName: String,
  val fromFunctionName: String
) : SymbolProcessor {

  private val Fail = object {
    fun CannotAnnotateParameter(symbol: KSNode, annotName: String) = logger.error("Cannot annotate the parameter ${symbol}. Only val-parameters are allowed to be annotated via @${annotName}.", symbol)
    fun CannotAnnotateMutableParameter(symbol: KSNode, annotName: String) = logger.error("Cannot annotate the mutable parameter ${symbol}. Only non-mutable parameters are allowed to be annotated via @${annotName}.", symbol)
    fun PropertyHasNoName(symbol: KSValueParameter, annotName: String) = logger.error("The property ${symbol} has no name. It must have a name to be annotated with @${annotName}.", symbol)
  }

  data class PropertyHolder(val name: String, val type: KSType, val fieldType: Type, val componentType: ComponentType, val annotationName: String) {
    sealed interface Type {
      object Constructor: Type
      object Member: Type
    }
    sealed interface ComponentType {
      object Component: ComponentType
      object MiddleComponent: ComponentType
      object ConstructorComponent: ComponentType
    }
    fun isRegularComponent() = componentType == ComponentType.Component
    fun isMiddleComponent() = componentType == ComponentType.MiddleComponent
    fun isConstructorComponent() = componentType == ComponentType.ConstructorComponent
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val symbols = resolver.getSymbolsWithAnnotation("io.decomat.Matchable")

    fun findComponents(sym: KSClassDeclaration): List<PropertyHolder> {
      val possibleAnnotations: List<String> = listOf(componentAnnotationName, middleComponentAnnotationName, constructorComponentAnnotationName)
      fun Sequence<KSAnnotation>.findAnnotations(): KSAnnotation? {
        val foundAnnots = filter { annot -> possibleAnnotations.contains(annot.shortName.getShortName()) }.toList()
        if (foundAnnots.size > 1) {
          logger.error("The symbol $sym has more than one @${possibleAnnotations} annotation. Only one is allowed.")
        }
        return foundAnnots.firstOrNull()
      }
      fun KSValueParameter.assertIsVal(): Unit = if (!isVal) Fail.CannotAnnotateMutableParameter(this, possibleAnnotations.toString()) else Unit
      fun KSValueParameter.assertHasName(): String = Fail.PropertyHasNoName(this, possibleAnnotations.toString()).let { "<???>" }
      fun KSPropertyDeclaration.assertNotMutable(): Unit = if (isMutable) Fail.CannotAnnotateMutableParameter(this, possibleAnnotations.toString()) else Unit
      fun componentType(annot: KSAnnotation) =
        when (annot.shortName.getShortName()) {
          componentAnnotationName -> PropertyHolder.ComponentType.Component
          middleComponentAnnotationName -> PropertyHolder.ComponentType.MiddleComponent
          constructorComponentAnnotationName -> PropertyHolder.ComponentType.ConstructorComponent
          else -> throw IllegalArgumentException("Unknown component type: ${annot.shortName.getShortName()}")
        }

      return (sym.primaryConstructor?.parameters?.mapNotNull { param ->
        val foundAnnot = param.annotations.findAnnotations()
        if (foundAnnot != null) param.assertIsVal()
        foundAnnot?.let { it to param }
      } ?: emptyList()).map { (foundAnnot, param) ->
        PropertyHolder(param.name?.getShortName() ?: param.assertHasName(), param.type.resolve(), PropertyHolder.Type.Constructor, componentType(foundAnnot), foundAnnot.shortName.getShortName())
      } +
      (sym.getAllProperties().mapNotNull { prop ->
        val foundAnnot = prop.annotations.findAnnotations()
        prop.assertNotMutable()
        foundAnnot?.let { it to prop }
      }).map { (foundAnnot, prop) ->
        PropertyHolder(prop.simpleName.getShortName(), prop.type.resolve(), PropertyHolder.Type.Member, componentType(foundAnnot), foundAnnot.shortName.getShortName())
      }
    }

    data class ComponentsToGen(val sym: KSClassDeclaration, val componentElements: List<PropertyHolder>, val useStarProjection: Boolean, val productComponents: KSPropertyDeclaration?)

    val componentsToGen =
      symbols
        .mapNotNull { sym ->
          when {
            sym is KSClassDeclaration && sym.classKind == ClassKind.CLASS -> {
              val useStarProjection =
                sym.annotations.find { it.shortName.getShortName() == matchableAnnotationName }?.arguments?.get(0)?.value as? Boolean ?: false

              val componentElements = findComponents(sym)
              // Only actually allowed to have one of these but keep a list anyhow

              if (componentElements.isEmpty()) {
                logger.error("No @Component parameters found in the primary constructor of the class $sym (They must be the annotation @${matchableAnnotationName})")
              }

              val hasProductClass = sym.superTypes.any { it.resolve().declaration.qualifiedName?.asString() == "io.decomat.HasProductClass" }
              val productClass = sym.superTypes.any { it.resolve().declaration.qualifiedName?.asString() == "io.decomat.ProductClass" }

              //logger.warn("Super types for: ${sym}: ${sym.superTypes.toList()}")
              //sym.superTypes.forEach { logger.warn("----------- Qualified Class: ${it.resolve().declaration.qualifiedName?.asString()}") }

              val productComponents =
                if (hasProductClass) {
                  val productComponents = sym.getDeclaredProperties().find { it.simpleName.getShortName() == "productComponents" }
                  if (productComponents == null) {
                    logger.error("The class $sym is a subtype of io.decomat.HasProductClass but does not have a property `productComponents` this should be impossible.")
                  }
                  productComponents
                }
                else null

              if (!hasProductClass && !productClass) {
                logger.error("""
                  The class $sym is not a subtype of io.decomat.HasProductClass or io.decomat.ProductClass.
                  In order to be able to annotate this class with @${matchableAnnotationName} do make it extend HasProductClass
                  and add a `productComponents` product approximately like this:
                  (plus/minus any generic parameters and any subclasses you may want to add...)
                  ---
                  data class $sym(...): HasProductClass<$sym> {
                    override val productComponents = productComponentsOf(this, ${componentElements.joinToString(", ")})
                    // Need at least an empty companion object, can put whatever you want inside
                    companion object { }
                  }
                """.trimIndent())
              }
              ComponentsToGen(sym, componentElements, useStarProjection, productComponents)
            }
            else ->
              null
          }
        }.toList()

    if (componentsToGen.isEmpty())
      logger.warn("No classes found with the @Matchable interface.")
    else {
      val description = componentsToGen.map { (cls, comps) -> "${cls.simpleName.getShortName()}(${comps.map { it.name }.joinToString(", ")})" }.joinToString(", ")
      logger.warn("Found the following classes/components with the @Matchable/@Component annotations: ${description}")
    }

    componentsToGen.forEach { (cls, members, useStarProjection, productComponents) ->
      val middleMembers = members.filter { it.isMiddleComponent() }
      if (middleMembers.size > 1) {
        logger.error("The Matchable class ${cls.simpleName.asString()} has more than one @${middleComponentAnnotationName} components (i.e. ${members.size}). No more than 1 is supported so far.")
      }

      val patternMatchMembers = members.filter { it.isRegularComponent() }
      if (patternMatchMembers.size > 2) {
        logger.error("The Matchable class ${cls.simpleName.asString()} has more than two @${componentAnnotationName} components (i.e. ${members.size}). No more than 2 are supported so far.")
      }

      generateExtensionFunction(GenModel.fromClassAndMembers(cls, members, useStarProjection, productComponents))
    }

    return listOf()
  }

  sealed interface ModelType {
    data class A(val a: Member): ModelType
    data class AB(val a: Member, val b: Member): ModelType
    data class AMB(val a: Member, val m: Member, val b: Member): ModelType
    object None: ModelType
  }

  data class GenModel private constructor(val imports: List<String>, val allMembers: List<Member>, val ksClass: KSClassDeclaration, val useStarProjection: Boolean, val productComponents: KSPropertyDeclaration?) {
    val members = allMembers.filter { it.field.isRegularComponent() }
    val middleMembers = allMembers.filter { it.field.isMiddleComponent() }

    val packageName = ksClass.packageName.asString()
    val className = ksClass.simpleName.asString()
    val fullClassName = ksClass.qualifiedName?.asString()
    // since we are going to add components to the imports, use regular name for the use-site
    // also, we want star projections e.g. for FlatMap<T, R> use FlatMap<*, *>
    val parametrizedName =
      if (ksClass.typeParameters.isNotEmpty())
      // Actually get the full projection of hte class e.g. Query<T> instead of Query<*>. Not sure how to actually get `Query<T>` as a string from a KSClassDeclaration
        "${ksClass.simpleName.getShortName()}<${ksClass.typeParameters.map { it.name.getShortName() }.joinToString(", ")}>" //.asStarProjectedType().toString()
      else
      // Otherwise it's not a star-projection and we can use the plain class name
        ksClass.toString()

    // using the logic above removes star projections but seems to be too strict logically
    val useSiteName =
      if (useStarProjection && ksClass.typeParameters.isNotEmpty())
        ksClass.asStarProjectedType().toString()
      else
        parametrizedName

    fun toModelType(): ModelType =
      when {
        members.size == 1 && middleMembers.isEmpty() -> ModelType.A(members[0])
        members.size == 2 && middleMembers.isEmpty() -> ModelType.AB(members[0], members[1])
        members.size == 2 && middleMembers.size == 1 -> ModelType.AMB(members[0], middleMembers[0], members[1])
        else -> ModelType.None
      }

    companion object {
      fun fromClassAndMembers(annotationHolderClass: KSClassDeclaration, params: List<PropertyHolder>, useStarProjection: Boolean, productComponents: KSPropertyDeclaration?): GenModel {

        fun parseRegularParam(param: PropertyHolder, ksClass: KSClassDeclaration): Member {
          val tpe = param.type

          // if it is used as a type-parameter for the class, don't use it
          val fullName = ksClass.qualifiedName?.asString()

          // for the parameters, if they have generic types you we want those to have starts e.g. Query<*> if it's Query<T>
          // NOTE: Removing `.starProjection()` makes type work (mostly) but seems to make matching too strict
          val name =
            tpe.let { paramTpe ->
              if (useStarProjection)
                paramTpe.starProjection().toString()
              else
                paramTpe.toString()
            }

          return Member(name, fullName, param, false)
        }

        val members = params.map {
          val decl = it.type.declaration
          when (decl) {
            is KSClassDeclaration -> parseRegularParam(it, decl)
            is KSTypeParameter -> Member(it.type.toString(), null, it, true)
            else -> throw IllegalArgumentException("Unknown declaration type: ${decl}")
          }
        }

        val classFullNameListElem =
          annotationHolderClass.qualifiedName?.asString()?.let { listOf(it) } ?: listOf()

        fun recurseGetArgs(type: KSType): List<KSType> =
          type.arguments.flatMap { arg ->
            arg.type?.resolve()?.let { argType ->
              when (argType.declaration) {
                is KSClassDeclaration -> listOf(argType) + recurseGetArgs(argType)
                else -> listOf()
              }
            } ?: listOf()
          }

        val additionalImports =
          classFullNameListElem +
            members.mapNotNull { it.qualifiedClassName } +
            // When elements themselves have things that need to be imported e.g. `data class Something(val content: List<OtherStuff>)` make sure to import `OtherStuff`
            members.flatMap {
              recurseGetArgs(it.field.type).map { it.declaration.qualifiedName?.asString() }.filterNotNull()
            }


        return GenModel(defaultImports + additionalImports.distinct(), members, annotationHolderClass, useStarProjection, productComponents)
      }

      val defaultImports = listOf(
        "io.decomat.Pattern",
        "io.decomat.Pattern1",
        "io.decomat.Pattern2",
        "io.decomat.Pattern2M",
        "io.decomat.Typed",
        "io.decomat.Is"
      )

    }
  }
  data class Member(val className: String, val qualifiedClassName: String?, val field: PropertyHolder, val isGeneric: Boolean) {
    val fieldName = field.name
  }

  /**
   * Based on the number of @Component/@MiddleComponent parameters, validate that the productComponents property is of the correct type
   * for example:
   * if there is one @Component parameter, the productComponents property must be of type io.decomat.ProductClass1
   * if there are two @Compoent parameters, the productComponents property must be of type io.decomat.ProductClass2
   * if there are two @Compoent parameters and one @MiddleComponent parameter, the productComponents property must be of type io.decomat.ProductClass2M
   */
  private fun validateProductComponentsType(modelType: ModelType, productComponents: KSPropertyDeclaration) {
    val productClassName = productComponents.type.resolve().declaration.qualifiedName?.asString()
    val parentClass = productComponents.parentDeclaration?.qualifiedName?.asString()
    val addendum = "Perhaps you are passing in too many or too few properties into the function `productComponentsOf`?"

    when {
      modelType is ModelType.A && productClassName != "io.decomat.ProductClass1" ->
        logger.error("The productComponents property must be of type io.decomat.ProductClass1 in the class ${parentClass} but instead it was ${productClassName}. ${addendum}", productComponents)
      modelType is ModelType.AB && productClassName != "io.decomat.ProductClass2" ->
        logger.error("The productComponents property must be of type io.decomat.ProductClass2 in the class ${parentClass} but instead it was ${productClassName}. ${addendum}", productComponents)
      modelType is ModelType.AMB && productClassName != "io.decomat.ProductClass2M" ->
        logger.error("The productComponents property must be of type io.decomat.ProductClass2M in the class ${parentClass} but instead it was ${productClassName}. ${addendum}", productComponents)
      else -> {}
    }
  }

  private fun generateExtensionFunction(model: GenModel) {
    val packageName = model.packageName
    val className = model.className
    val classUseSiteName = model.useSiteName
    val companionName = model.className
    val members = model.members
    val allMembers = model.allMembers

    val modelType = model.toModelType()

    if (model.productComponents != null) validateProductComponentsType(modelType, model.productComponents)

    val file = codeGenerator.createNewFile(
      Dependencies.ALL_FILES, packageName, "${className}DecomatExtensions"
    )

    file.bufferedWriter().use { writer ->
      fun eachLetter(f: Member.(String) -> String) =
        model.members.filter { it.field.isRegularComponent() }.withIndex().map { (num, member) ->
          val letter = ('a' + num).uppercase()
          f(member, letter)
        }

      val memberParams = model.members.map { it.className }

      val typeParams =
        if (model.useStarProjection) {
          model.ksClass.typeParameters
            // only use top-level parameters defined in our classes since we are star-projecting everything inside
            // e.g. if our class is FlatMap<Query<T>, Query<R>> we don't want to include the T and R, in the parameters
            // list because the match will actually be done on FlatMap<Query<*>, Query<*>> and the `operator function get`
            // that returns FlatMap_M won't know what these parameters are requiring them to be specified explicitly.
            // On the other hand, if it's a top-level like Entity<T> then we should include it.
            .filter { memberParams.contains(it.name.getShortName()) }
            .mapNotNull { it.name.asString() }
        } else {
          model.ksClass.typeParameters.map { it.name.getShortName() }
        }

      //logger.warn("---------- Type Params: ${typeParams}")

      val mString = if (model.middleMembers.isNotEmpty()) "M" else ""

      // Generate: A: Pattern<AP>, B: Pattern<BP>
      val pats =
        when (modelType) {
          is ModelType.A -> "A: Pattern<AP>"
          is ModelType.AB -> "A: Pattern<AP>, B: Pattern<BP>"
          is ModelType.AMB -> "A: Pattern<AP>, B: Pattern<BP>" // No M is defined here because it is a concrete type in patLetters below
          is ModelType.None -> ""
        }

      fun tryQualified(m: DecomatProcessor.Member): String =
        m.qualifiedClassName ?: run {
          logger.warn("Fully qualified name of `${m.field.type.toString()}` was null using regular name: ${m.className}")
          m.className
        }

      val patLetters =
        when (modelType) {
          // e.g. the A in Pattern1<A, AP, FlatMap>
          is ModelType.A -> listOf("A")
          // e.g. the A and B in Pattern2<A, B, AP, BP, FlatMap>
          is ModelType.AB -> listOf("A", "B")
          // e.g. the A and String and B in Pattern2M<A, String, B, AP, BP, FlatMap>
          // I.e. the actual M-parameter is a concrete type per the data-class that XYZ_M class is being defined for
          is ModelType.AMB -> listOf("A", tryQualified(modelType.m), "B")
          is ModelType.None -> listOf()
        }

      fun List<String>.commaSep() = joinToString(", ")

      // Generate: AP: Query, BP: Query
      val patTypes = (eachLetter { "${it}P: ${this.className}" } + typeParams).commaSep()
      // Generate: Pattern2<A, B, AP, BP, FlatMap>
      val subClass = "Pattern${model.members.size}${mString}<${(patLetters + eachLetter { "${it}P" } + listOf(classUseSiteName)).commaSep()}>"
      // Generate: a: A, b: B
      val classValsTypes = eachLetter { "${it.lowercase()}: $it" }.commaSep()
      // Generate: a, b
      val classVals = eachLetter { it.lowercase() }.commaSep()

      val memberKeyValues =
        allMembers.map { "${it.field.name}: ${it.field.type.toString()}" }.commaSep()


      val (genericBlock, hasGenerics) = run {
        //val genericFieldParams = allMembers.filter { it.isGeneric }.map { it.qualifiedClassName ?: it.className }
        val genericClassParams = model.ksClass.typeParameters.map { it.toString() }
        val genericParams = genericClassParams
        val hasGenerics = genericParams.isNotEmpty()
        val genericBlock =
          if (hasGenerics) "<${genericParams.commaSep()}>" else ""
        genericBlock to hasGenerics
      }

      val isExpression =
        if (!hasGenerics) """val ${companionName}.Companion.Is get() = Is<$classUseSiteName>()""" else ""

      writer.apply {
        // class FlatMap_M<A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query>(a: A, b: B): Pattern2<A, B, AP, BP, FlatMap>(a, b, Typed<FlatMap>())
        //operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> FlatMap.Companion.get(a: A, b: B) = FlatMap_M(a, b)

        // include the '            ' in joinToString below since that is the margin of the fileContent variable that needs to be in
        // front of everything, otherwise it won't be stripped properly
        val fileContent =
          """
            package $packageName
            
            ${model.imports.map { "import $it" }.joinToString("\n            ")}
            
            class ${className}_M<$pats, $patTypes>($classValsTypes): $subClass($classVals, Typed<$classUseSiteName>())
            operator fun <$pats, $patTypes> ${companionName}.Companion.get($classValsTypes) = ${className}_M($classVals)
            ${isExpression}             
          """.trimIndent()

        val adtFunctions =
          """
            class Copy${className}${genericBlock}(${allMembers.map { "val ${it.fieldName}: ${it.field.type.toString()}" }.commaSep()}) {
              operator fun invoke(original: ${model.parametrizedName}) =
                if (${allMembers.map {"original.${it.fieldName} == ${it.fieldName}"}.joinToString(" && ")})
                  original
                else
                  original.copy(${allMembers.map { "${it.fieldName} = ${it.fieldName}" }.commaSep()})  
            }
            
            // Helper function for ADTs that allows you to easily copy the element mentioning only the properties you care about
            // Typically in an ADT you have a lot of properties and only one or two define the actual structure of the object
            // this means that you want to explicitly state only the structural ADT properties and implicitly copy the rest.
            fun ${genericBlock} ${companionName}.Companion.${fromFunctionName}(${memberKeyValues}) = Copy${className}(${allMembers.map { it.fieldName }.commaSep()})
              
            // A "Copy from Self" Helper function for ADTs that allows you to copy an element X from inside of a this@X
            // e.g. if you have a data class FlatMap(val head: Query, val id: String, val body: Query)
            // you can copy it from inside of a FlatMap (i.e. a this@FlatMap) like this: FlatMap.fromHere(head, id, body)
            context(${model.parametrizedName}) fun ${genericBlock} ${companionName}.Companion.${fromHereFunctionName}(${memberKeyValues}) =
              Copy${className}(${allMembers.map { it.fieldName }.commaSep()}).invoke(this@${className})
              
            data class Id${className}${genericBlock}(${allMembers.map { "val ${it.fieldName}: ${it.className}" }.commaSep()})
              
            // A helper function that creates a ADT-identifier from the structural properties of the ADT.
            // Typically in an ADT you have a lot of properties and only one or two define the actual structure of the object
            // and those are the only ones on which you want the `equals` and `hashCode` to be based.
            fun ${genericBlock} ${companionName}${genericBlock}.id() =
              Id${className}${genericBlock}(${allMembers.map { "this.${it.fieldName}" }.commaSep()})
          """.trimIndent()

        write(fileContent + if (renderAdtFunctions) "\n" + adtFunctions else "")
      }
    }
  }
}
