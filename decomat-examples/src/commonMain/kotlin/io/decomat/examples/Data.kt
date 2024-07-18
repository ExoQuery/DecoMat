package io.decomat.examples

import io.decomat.*

sealed interface Name
@Matchable
data class SimpleName(@Component val first: String, @Component val last: String): Name, HasProductClass<SimpleName> {
  override val productComponents = productComponentsOf(this, first, last)
  companion object { }
}

@Matchable
data class FullName(@Component val first: String, val middle: String, @Component val last: String): Name, HasProductClass<FullName> {
  override val productComponents = productComponentsOf(this, first, last)
  companion object { }
}

@Matchable
data class Person(@Component val name: Name, @Component val age: Int): HasProductClass<Person> {
  override val productComponents = productComponentsOf(this, name, age)
  companion object {}
}

fun usingGen(): String {
  val x = FullName_M(Is("foo"), Is("bar"))
  return "doesn't matter"
}
