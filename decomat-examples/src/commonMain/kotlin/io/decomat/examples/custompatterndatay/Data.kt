package io.decomat.examples.custompatterndatay

import io.decomat.*
import io.decomat.examples.custompatterndatay.FullName_M

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

fun <A: Pattern<AP>, B: Pattern<BP>, AP: String, BP: String> FullName.Companion.getget(aa: A, bbbb: B) = FullName_M(aa, bbbb)

object FirstLast {
  fun blahblah(): Pattern2<Pattern0<String>, Pattern0<String>, String, String, Name> {
    //val x = FullName_M(Is("foo"), Is("bar"))
    return TODO()
  }

  //fun foofoo() {
  //  val x = FullName_M(Is("foo"), Is("bar"))
  //}
}