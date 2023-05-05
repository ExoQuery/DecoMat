package io.decomat

import io.decomat.manual.*


object ThenTest {
  fun test1() = FlatMap_M(Is(), Is()).then { a, b -> "foo" }
  fun test1B() = FlatMap_M(Is(), Is()).thenBoth {{ a, b -> body }}
  fun test2() = FlatMap_M(Is(), Is<Map>()).then { a, b -> "foo" }

  fun test3() {
    FlatMap_M(Is(), Map_M(Is(), Is())).thenThis { fm -> "foo" }

    // TODO hard type these make it it works right (in the template-tests)
    FlatMap_M(Is(), Map_M(Is(), Is())).then { a, b -> "foo" }
    FlatMap_M(Is(), Map_M(Is(), Is<Map>())).then { a, b -> "foo" }
    FlatMap_M(Is(), Map_M(Is(), Is<Map>())).then { a, (b1, b2) -> "foo" }
    FlatMap_M(Distinct_M(Is()), Map_M(Is(), Is<Map>())).then { (a), (b1, b2) -> "foo" }
  }

  fun myFun() {
    on("foo").match(
      FlatMap_M(Is(), Map_M(Is(), Is<Map>())).then { a, (b, c) -> "foobar" }
    )
  }

}

fun main() {
  val foo = Entity("foo")
  val bar = Entity("bar")
  val baz = Entity("baz")
  val waz = Entity("waz")
  val kaz = Entity("kaz")

  data class Name(val first: String, val last: String)
  data class Person(val name: Name, val age: Int)

  // case Map(a: Query, b, c) if (b == c) =>

  /*
  on(query).match {
    Map[Any<Query>(), Any(), Any()].when(...).then(...)
  }


  on(person).match {
    Person[Name[Any(), Any()], Any()]
      .then { (first, last), age -> ... }

   */


  val case =
    FlatMap[Distinct[Is()], Map[Is(), Is()]]
      .then { (a), (b1, b2) -> println("Matched: $a, ($b1, $b2)") }

  case.eval(FlatMap(Distinct(foo), Map(bar, baz)))

//  println( FlatMap_M(Any(), Map_M(Any(), Any<Map>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz)))) )
//  println( FlatMap_M(Any(), Map_M(Any(), Any<Map>())).matches(FlatMap(foo, Map(foo, foo))) )
//  println( FlatMap_M(Any(), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
//
//  println( FlatMap[Any(), Map[Any(), Any<Map>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))) )
//  println( FlatMap[Any(), Map[Any(), Any<Entity>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))) )
//
//  println( FlatMap[Any(), Map[Any(), Any.Map]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))) )
//
//  println( FlatMap_M(Any(), Map_M(Any(), Any<Map>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz)))) )
//  println( FlatMap_M(Any(), Map_M(Any(), Any<Map>())).matches(FlatMap(foo, Map(foo, foo))) )
//  println( FlatMap_M(Any(), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
//
//  println( FlatMap_M(Any(foo), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
//  println( FlatMap_M(Any(Entity("bar")), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
//
//  println( FlatMap_M(Any<Entity>(), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
//  println( FlatMap_M(Any<Map>(), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
}