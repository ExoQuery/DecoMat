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
    val symbols = resolver.getSymbolsWithAnnotation("Extended")
    val generatedSymbols = mutableListOf<KSAnnotated>()

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
        }

//      .filter { it is KSClassDeclaration && it.classKind == ClassKind.CLASS }
//      .forEach { ksClass ->
//        val hasExtendedMethod = ksClass.declarations.any {
//          it.hasAnnotation("ExtendedMethod") && it is KSFunctionDeclaration
//        }
//        if (hasExtendedMethod) {
//          generateExtensionFunction(ksClass as KSClassDeclaration)
//          generatedSymbols.add(ksClass)
//        }
//      }

    return generatedSymbols
  }

  private fun generateExtensionFunction(ksClass: KSClassDeclaration, members: List<KSValueParameter>) {
    val packageName = ksClass.packageName.asString()
    val className = ksClass.simpleName.asString()

    val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, packageName, "$className.kt")

    file.bufferedWriter().use { writer ->
      fun eachLetter(f: KSValueParameter.(String) -> String) =
        members.withIndex().map { (num, member) ->
          val letter = ('a' + num).uppercase()
          f(member, letter)
        }.joinToString(", ")

      // Generate: A: Pattern<AP>, B: Pattern<BP>
      val pats = eachLetter { "$it, Pattern<${it}P>" }
      // Generate: AP: Query, BP: Query
      val patTypes = eachLetter { "${it}P: ${className}" }
      // Generate: Pattern2<A, B, AP, BP, FlatMap>
      val subClass = "Pattern${members.size}<${eachLetter { it.lowercase() }}, ${eachLetter { "${it}P" }}, $className>"
      // Generate: a: A, b: B
      val classValsTypes = eachLetter { "${it.lowercase()}: $it" }
      // Generate: a, b
      val classVals = eachLetter { it.lowercase() }

      writer.apply {
        // class FlatMap_M<A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query>(a: A, b: B): Pattern2<A, B, AP, BP, FlatMap>(a, b, Typed<FlatMap>())
        val fileContent =
          """
            package $packageName
            
            class ${className}_M<$pats, $patTypes>($classValsTypes): $subClass($classVals, Typed<$className>())
          """.trimIndent()

        write(fileContent)
      }
    }
  }
}
