package io.decomat.examples.custompattern

import io.decomat.*
import io.decomat.examples.querydsl.Entity
import io.decomat.examples.querydsl.Entity_M

sealed interface Name
data class SimpleName(val first: String, val last: String): Name
data class FullName(val first: String, val middle: String, val last: String): Name

@Matchable
data class Person(@Component val name: Name, @Component val age: Int): HasProductClass<Person> {
  override val productComponents = productComponentsOf(this, name, age)
  companion object {}
}

data class FirstName(val first: Pattern0<String>): Pattern1<Pattern0<String>, String, Name>(first, Typed<Name>()) {
  val matchName: (Name) -> Components1<String>? = {
    when(it) {
      is SimpleName -> Components1(it.first)
      is FullName -> Components1(it.first)
      // TODO a case where the match fails
    }
  }

  // Not strictly necessary but allows you to use bracket syntax e.g. FirstName[value] instead of FirstName(value)
  override fun matches(comps: ProductClass<Name>): Boolean =
    matchName(comps.value) != null

  override fun divideIntoComponents(instance: ProductClass<Name>): Components1<String> =
    matchName(instance.value) ?: failToDivide(instance)

  override fun divideIntoComponentsAny(instance: kotlin.Any): Components1<String> =
    when(instance) {
      is Name -> matchName(instance) ?: failToDivide(instance)
      else -> failToDivide(instance)
    }

  companion object {
    operator fun get(a: Pattern0<String>) = FirstName(a)
  }
}

fun main() {
  val p = Person(SimpleName("John", "Doe"), 42)

  val out =
    on(p).match(
      case(Person[FirstName[Is()], Is()]).then { (firstName), age -> Pair(firstName, age) }
    )

  println(out)
}


