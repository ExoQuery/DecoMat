package io.decomat.middle

import io.decomat.*


data class Name(val first: String, val middle: Int, val last: String): HasProductClass<Name> {
  override val productComponents = ProductClass2M(this, first, middle, last)
  companion object {
  }
}
@Suppress("FINAL_UPPER_BOUND")
class Name_M<P1: Pattern<A1>, P2: Pattern<A2>, A1: String, A2: String>(a: P1, b: P2): Pattern2M<P1, Int, P2, A1, A2, Name>(a, b, Typed<Name>())
@Suppress("FINAL_UPPER_BOUND")
operator fun <P1: Pattern<A1>, P2: Pattern<A2>, A1: String, A2: String> Name.Companion.get(a: P1, b: P2): Pattern2M<P1, Int, P2, A1, A2, Name> = Name_M(a, b)

data class Person(val name: Name, val suffix: String, val age: Int): HasProductClass<Person> {
  override val productComponents = ProductClass2M(this, name, suffix, age)

  companion object {
  }
}

@Suppress("FINAL_UPPER_BOUND")
class Person_M<P1: Pattern<A1>, P2: Pattern<A2>, A1: Name, A2: Int>(a: P1, b: P2): Pattern2M<P1, String, P2, A1, A2, Person>(a, b, Typed<Person>())
@Suppress("FINAL_UPPER_BOUND")
operator fun <P1: Pattern<A1>, P2: Pattern<A2>, A1: Name, A2: Int> Person.Companion.get(a: P1, b: P2): Pattern2M<P1, String, P2, A1, A2, Person> = Person_M(a, b)
