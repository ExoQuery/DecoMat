package io.decomat.examples

import io.decomat.*

@Matchable
data class FullName(@Component val first: String, val middle: String, @Component val last: String): HasProductClass<FullName> {
  override val productComponents = productComponentsOf(this, first, last)
  companion object { }
}

fun usingGen(): String {
  val x = FullName_M(Is("foo"), Is("bar"))
  return "doesn't matter"
}
