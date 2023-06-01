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


inline fun <P1 : Pattern<R1>, R1, reified R> customPattern1(
  nested1: P1,
  noinline matchName: (R) -> Components1<R1>?
) = CustomPattern1(nested1, matchName, Typed<R>())

object FirstName2 {
  operator fun get(nameString: Pattern0<String>) =
    customPattern1(nameString) { it: Name ->
      when(it) {
        is SimpleName ->
          if (nameString.matchesAny(it.first))
            Components1(it.first)
          else
            null
        is FullName ->
          if (nameString.matchesAny(it.first))
            Components1(it.first)
          else
            null
        else ->
          null
      }
    }
}

//fun FirstName2(nameString: Pattern0<String>) =
//  customPattern1(nameString) { it: Name ->
//    when(it) {
//      is SimpleName ->
//        if (nameString.matchesAny(it.first))
//          Components1(it.first)
//        else
//          null
//      is FullName ->
//        if (nameString.matchesAny(it.first))
//          Components1(it.first)
//        else
//          null
//      else ->
//        null
//    }
//  }

class CustomPattern1<P1 : Pattern<R1>, R1, R>(
  val nested1: P1,
  val matchName: (R) -> Components1<R1>?,
  val tpe: Typed<R>
): Pattern1<P1, R1, R>(nested1, tpe) {
  override fun matches(comps: ProductClass<R>): Boolean =
    matchName(comps.value) != null

  override fun divideIntoComponents(instance: ProductClass<R>): Components1<R1> =
    matchName(instance.value) ?: failToDivide(instance)

  override fun divideIntoComponentsAny(instance: kotlin.Any): Components1<R1> =
    if (isType(instance, tpe.type))
      matchName(instance as R) ?: failToDivide(instance)
    else
      failToDivide(instance)
}



data class FirstName(val nameString: Pattern0<String>): Pattern1<Pattern0<String>, String, Name>(nameString, Typed<Name>()) {
  val matchName: (Name) -> Components1<String>? = {
    when(it) {
      is SimpleName ->
        // TODO If the thing inside nameString is a ProductClass don't need to create a ProductClass0 first
        //      probably can do that with an `it is ProductClass` flag
        if (nameString.matchesAny(it.first))
          Components1(it.first)
        else
          null
      is FullName ->
        if (nameString.matchesAny(it.first))
          Components1(it.first)
        else
          null
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
      case(Person[FirstName2[Is("Jack")], Is()]).then { (firstName), age -> Pair(firstName, age) }
    )

  println(out)

  val out1 =
    on(p).match(
      case(Person[FirstName2[Is("John")], Is()]).then { (firstName), age -> Pair(firstName, age) }
    )

  println(out1)
}



