package io.decomat

import com.google.devtools.ksp.innerArguments
import com.google.devtools.ksp.isLocal
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

class DecomatProcessor(
  private val logger: KSPLogger,
  val codeGenerator: CodeGenerator
) : SymbolProcessor {

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val symbols = resolver.getSymbolsWithAnnotation("io.decomat.Matchable")
    val componentsToGen =
      symbols
        .mapNotNull { sym ->
          when {
            sym is KSClassDeclaration && sym.classKind == ClassKind.CLASS -> {
              // TODO also allow annotation on values in body (maybe even methods?)
              val componentElements =
                sym.primaryConstructor?.parameters?.filter {
                  val isVal = it.isVal
                  if (!isVal) logger.error("Cannot introspect the parameter. Only val-parameters are allowed.", it)
                  val hasAnnotation = it.annotations.any { annot -> annot.shortName.getShortName() == "Component" }
                  isVal && hasAnnotation
                } ?: emptyList()

              Pair(sym as KSClassDeclaration, componentElements)
            }
            else ->
              null
          }
        }.toList()

    if (componentsToGen.isEmpty())
      logger.warn("No classes found with the @Matchable interface.")
    else {
      val description = componentsToGen.map { (cls, comps) -> "${cls.simpleName.getShortName()}(${comps.map { it.name?.getShortName() ?: "<Unknown-Name>" }.joinToString(", ")})" }.joinToString(", ")
      logger.warn("Found the following classes/components with the @Matchable/@Component annotations: ${description}")
    }

    componentsToGen.forEach { (cls, members) ->
      // TODO Need to test
      if (members.size > 3) {
        logger.error("The Matchable class ${cls.simpleName.asString()} has more than 3 components (i.e. ${members.size}). No more than 3 are supported so far.")
      }

      generateExtensionFunction(GenModel.fromClassAndMembers(cls, members))
    }

    return listOf()
  }

  data class GenModel private constructor(val imports: List<String>, val members: List<Member>, val ksClass: KSClassDeclaration) {
    val packageName = ksClass.packageName.asString()
    val className = ksClass.simpleName.asString()
    val fullClassName = ksClass.qualifiedName?.asString()
    // since we are going to add components to the imports, use regular name for the use-site
    // also, we want star projections e.g. for FlatMap<T, R> use FlatMap<*, *>

    //val useSiteName = "${ksClass.simpleName.getShortName()}<${ksClass.typeParameters.map { it.name.getShortName() }.joinToString(", ")}>" //.asStarProjectedType().toString()
    // using the logic above removes star projections but seems to be too strict logically
    val useSiteName = ksClass.asStarProjectedType().toString()

      companion object {
      fun fromClassAndMembers(ksClass: KSClassDeclaration, ksParams: List<KSValueParameter>): GenModel {
        val members = ksParams.map { param ->
          val tpe = param.type.resolve()
          val decl = tpe.declaration
          val typeParamNames = ksClass.typeParameters.map { it.qualifiedName }

          // if it is used as a type-parameter for the class, don't use it
          val fullName =
            if (typeParamNames.contains(decl.qualifiedName))
              null
            else
              decl.qualifiedName?.asString()

          // for the parameters, if they have generic types you we want those to have starts e.g. Query<*> if it's Query<T>
          // NOTE: Removing `.starProjection()` makes type work (mostly) but seems to make matching too strict
          val name = param.type.resolve().starProjection().toString()

          Member(name, fullName, param)
        }

        val classFullNameListElem =
          ksClass.qualifiedName?.asString()?.let { listOf(it) } ?: listOf()

        val additionalImports =
          classFullNameListElem + members.mapNotNull { it.fullName }

        return GenModel(defaultImports + additionalImports.distinct(), members, ksClass)
      }

      val defaultImports = listOf(
        "io.decomat.Pattern",
        "io.decomat.Pattern1",
        "io.decomat.Pattern2",
        "io.decomat.Pattern3",
        "io.decomat.Typed",
        "io.decomat.Is"
      )

    }
  }
  data class Member(val name: String, val fullName: String?, val ksParam: KSValueParameter) {
    // since we are going to add components to the imports, use regular name for the use-site
    val useSiteName = name
  }

  private fun generateExtensionFunction(model: GenModel) {
    val packageName = model.packageName
    val className = model.className
    val classUseSiteName = model.useSiteName
    val companionName = model.className

    val file = codeGenerator.createNewFile(
      Dependencies.ALL_FILES, packageName, "${className}DecomatExtensions"
    )

    file.bufferedWriter().use { writer ->
      fun eachLetter(f: Member.(String) -> String) =
        model.members.withIndex().map { (num, member) ->
          val letter = ('a' + num).uppercase()
          f(member, letter)
        }.joinToString(", ")

      val memberParams = model.members.map { it.name }

      val typeParams =
        model.ksClass.typeParameters
          // only use top-level parameters defined in our classes since we are star-projecting everything inside
          // e.g. if our class is FlatMap<Query<T>, Query<R>> we don't want to include the T and R, in the parameters
          // list because the match will actually be done on FlatMap<Query<*>, Query<*>> and the `operator function get`
          // that returns FlatMap_M won't know what these parameters are requiring them to be specified explicitly.
          // On the other hand, if it's a top-level like Entity<T> then we should include it.
          .filter { memberParams.contains(it.name.getShortName()) }
          .mapNotNull { it.name.asString() }

      //logger.warn("---------- Type Params: ${typeParams}")

      // Generate: A: Pattern<AP>, B: Pattern<BP>
      val pats = eachLetter { "$it: Pattern<${it}P>" }
      // Generate: AP: Query, BP: Query
      val patTypes = eachLetter { "${it}P: ${this.useSiteName}" } + if (typeParams.isNotEmpty()) ", " + typeParams.joinToString(", ") else ""
      // Generate: Pattern2<A, B, AP, BP, FlatMap>
      val subClass = "Pattern${model.members.size}<${eachLetter { it.uppercase() }}, ${eachLetter { "${it}P" }}, $classUseSiteName>"
      // Generate: a: A, b: B
      val classValsTypes = eachLetter { "${it.lowercase()}: $it" }
      // Generate: a, b
      val classVals = eachLetter { it.lowercase() }


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
            val ${companionName}.Companion.Is get() = Is<$classUseSiteName>()
             
          """.trimIndent()

        write(fileContent)
      }
    }
  }
}
