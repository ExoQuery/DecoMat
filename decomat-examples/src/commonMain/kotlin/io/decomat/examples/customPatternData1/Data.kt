package io.decomat.examples.customPatternData1

import io.decomat.*

sealed interface Name
data class SimpleName(val first: String, val last: String): Name
data class FullName(val first: String, val middle: String, val last: String): Name

@Matchable
data class Person(@Component val name: Name, @Component val age: Int): HasProductClass<Person> {
  override val productComponents = productComponentsOf(this, name, age)
  companion object {}
}

@Matchable
data class Person1(@Component val name: Name): HasProductClass<Person1> {
  override val productComponents = productComponentsOf(this, name)
  companion object {}
}

// Cannot have this here because it causes: https://youtrack.jetbrains.com/issue/KT-70025/KMPKSP-JavaScript-compile-fails-with-Cannot-read-properties-of-undefined
//object FirstName2 {
//  operator fun get(nameString: Pattern0<String>) =
//    customPattern1(nameString) { it: Name ->
//      when(it) {
//        is SimpleName -> Components1(it.first)
//        is FullName -> Components1(it.first)
//      }
//    }
//}