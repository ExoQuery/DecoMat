package io.decomat

data class Name(val first: String, val last: String): HasProductClass<Name> {
  override val productComponents = ProductClass2(this, first, last)
  companion object {
  }
}
@Suppress("FINAL_UPPER_BOUND")
class Name_M<P1: Pattern<A1>, P2: Pattern<A2>, A1: String, A2: String>(a: P1, b: P2): Pattern2<P1, P2, A1, A2, Name>(a, b, Typed<Name>())
@Suppress("FINAL_UPPER_BOUND")
operator fun <P1: Pattern<A1>, P2: Pattern<A2>, A1: String, A2: String> Name.Companion.get(a: P1, b: P2): Pattern2<P1, P2, A1, A2, Name> = Name_M(a, b)

data class Person(val name: Name, val age: Int): HasProductClass<Person> {
  override val productComponents = ProductClass1(this, name)

  companion object {
  }
}

@Suppress("FINAL_UPPER_BOUND")
class Person_M<P1: Pattern<A1>, A1: Name>(a: P1): Pattern1<P1, A1, Person>(a, Typed<Person>())
@Suppress("FINAL_UPPER_BOUND")
operator fun <P1: Pattern<A1>, A1: Name> Person.Companion.get(a: P1): Pattern1<P1, A1, Person> = Person_M(a)
