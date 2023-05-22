package io.decomat

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

    if (componentsToGen.isEmpty()) logger.warn("No classes found with the @Matchable interface.")
    else {
      val description = componentsToGen.map { (cls, comps) -> "${cls.simpleName.getShortName()}(${comps.map { it.name?.getShortName() ?: "<Unknown-Name>" }.joinToString(", ")})" }.joinToString(", ")
      logger.warn("Found the following classes/components with the @Matchable/@Component annotations: ${description}")
    }

    componentsToGen.forEach { (cls, members) ->
      generateExtensionFunction(cls, members)
    }

    return listOf()
  }

  private fun generateExtensionFunction(ksClass: KSClassDeclaration, members: List<KSValueParameter>) {
    val packageName = ksClass.packageName.asString()
    val className = ksClass.simpleName.asString()
    val fullClassName = ksClass.qualifiedName?.asString()

    val file = codeGenerator.createNewFile(
      Dependencies.ALL_FILES, packageName, "${className}DecomatExtensions"
    )

    file.bufferedWriter().use { writer ->
      fun eachLetter(f: KSValueParameter.(String) -> String) =
        members.withIndex().map { (num, member) ->
          val letter = ('a' + num).uppercase()
          f(member, letter)
        }.joinToString(", ")

      // Generate: A: Pattern<AP>, B: Pattern<BP>
      val pats = eachLetter { "$it: Pattern<${it}P>" }
      // Generate: AP: Query, BP: Query
      val patTypes = eachLetter { "${it}P: ${this.type.resolve().declaration.qualifiedName?.asString() ?: "<Unknown-Name-Type: ${this.type.element}>"}" }
      // Generate: Pattern2<A, B, AP, BP, FlatMap>
      val subClass = "Pattern${members.size}<${eachLetter { it.uppercase() }}, ${eachLetter { "${it}P" }}, $fullClassName>"
      // Generate: a: A, b: B
      val classValsTypes = eachLetter { "${it.lowercase()}: $it" }
      // Generate: a, b
      val classVals = eachLetter { it.lowercase() }

      writer.apply {
        // class FlatMap_M<A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query>(a: A, b: B): Pattern2<A, B, AP, BP, FlatMap>(a, b, Typed<FlatMap>())
        val fileContent =
          """
            package $packageName
            
            import io.decomat.Pattern
            import io.decomat.Pattern1
            import io.decomat.Pattern2
            import io.decomat.Pattern3
            import io.decomat.Typed
            
            class ${className}_M<$pats, $patTypes>($classValsTypes): $subClass($classVals, Typed<$fullClassName>())
          """.trimIndent()

        write(fileContent)
      }
    }
  }
}
