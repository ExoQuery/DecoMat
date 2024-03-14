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
      environment.options.get("constructorComponentAnnotationName") ?: "ConstructorComponent"
    return DecomatProcessor(
      environment.logger, environment.codeGenerator,
      matchableAnnotationName, componentAnnotationName, middleComponentAnnotationName, constructorComponentAnnotationName
    )
  }
}