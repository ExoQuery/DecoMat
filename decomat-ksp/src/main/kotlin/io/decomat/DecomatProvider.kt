package io.decomat

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class DecomatProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    val matchableAnnotationName =
      environment.options.get("matchableName") ?: "Matchable"
    val componentAnnotationName =
      environment.options.get("componentName") ?: "Component"
    val middleComponentAnnotationName =
      environment.options.get("middleComponentName") ?: "MiddleComponent"
    val constructorComponentAnnotationName =
      environment.options.get("constructorComponentName") ?: "ConstructorComponent"
    val renderAdtFunctions =
      environment.options.get("renderAdtFunctions")?.toBoolean() ?: false
    val renderFromHereFunction =
      renderAdtFunctions && (environment.options.get("renderFromHereFunction")?.toBoolean() ?: false)
    val fromHereFunctionName =
      environment.options.get("fromHereFunctionName") ?: "fromHere"
    val fromFunctionName =
      environment.options.get("fromFunctionName") ?: "from"

    return DecomatProcessor(
      environment.logger, environment.codeGenerator,
      matchableAnnotationName,
      componentAnnotationName,
      middleComponentAnnotationName,
      constructorComponentAnnotationName,
      renderAdtFunctions,
      renderFromHereFunction,
      fromHereFunctionName,
      fromFunctionName
    )
  }
}