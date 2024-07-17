package io.decomat.examples.customPatternData2

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

fun <A: Pattern<AP>, B: Pattern<BP>, AP: String, BP: String> FullName.Companion.getget(aa: A, bbbb: B) = FullName_M(aa, bbbb)

class FullName_MM<A: Pattern<AP>, B: Pattern<BP>, AP: String, BP: String>(a: A, b: B): Pattern2<A, B, AP, BP, FullName>(a, b, Typed<FullName>())

object FirstLast {
  fun blahblah(first: Pattern0<String>, last: Pattern0<String>) =
    customPattern2(first, last) { it: Name ->
      on(it).matchOne(
        case(FullName_MM(Is("foo"), Is("bar"))).then { first, last -> Components2(first, last) }
//        case(FullName.getget(Is(), Is())).then { first, last -> Components2(first, last) }//,
//        case(SimpleName[Is(), Is()]).then { first, last -> Components2(first, last) }
      )
    }
}