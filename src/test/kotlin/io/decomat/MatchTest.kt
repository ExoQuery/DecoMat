package io.decomat

import io.decomat.*
import io.decomat.Map
import io.decomat.manual.*
import org.junit.jupiter.api.Nested


import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("DANGEROUS_CHARACTERS")
internal class MatchTest {
  val foo = Entity("foo")
  val bar = Entity("bar")
  val baz = Entity("baz")
  val waz = Entity("waz")
  val kaz = Entity("kaz")


  @Nested
  inner class `flatMap(?,Map(Map))` {
    @Test
    fun `flatMap(?, ?)`() {
      assertTrue(FlatMap_M(Any(), Any()).matches(FlatMap(foo, bar)))
      assertTrue(FlatMap_M(Any(), Any()).matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))
    }

    @Test
    fun `flatMap(?, Map(?))`() =
      assertTrue(
        FlatMap_M(Any(), Any()).matches(FlatMap(foo, Map(bar, baz)))
      )

    @Test
    fun `flatMap(?, Map(Map)`() =
      assertTrue(
        FlatMap_M(Any(), Map_M(Any(), Any<Map>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz))))
      )

    @Test
    fun `flatMap(?, Map(!Entity) - Negative`() =
      assertFalse(
        FlatMap_M(Any(), Map_M(Any(), Any<Entity>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz))))
      )

    @Test
    fun `flatMap(?, Map(!Map) - Negative`() =
      assertFalse(
        FlatMap_M(Any(), Map_M(Any(), Any<Map>())).matches(FlatMap(foo, Map(foo, foo)))
      )

    @Test
    fun `flatMap(foo, Map(?))`() =
      assertTrue(
        FlatMap_M(Any(foo), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo)))
      )

    @Test
    fun `flatMap(!foo, Map(?)) - Negative`() =
      assertFalse(
        FlatMap_M(Any(bar), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo)))
      )
  }

  @Nested
  inner class BracketSyntax {
    @Test
    fun `flatMap(?, Map(Map))`() =
      assertTrue(FlatMap[Any(), Map[Any(), Any<Map>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

    @Test
    fun `flatMap(?, Map(!Map))`() =
      assertFalse(FlatMap[Any(), Map[Any(), Any<Entity>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

    @Test
    fun `flatMap(?, Map(Is_Map))`() =
      assertTrue(FlatMap[Any(), Map[Any(), Any.Map]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

  }
}




fun main() {


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
    FlatMap[Distinct[Any()], Map[Any(), Any()]]
      .then { (a), (b1, b2) -> println("Matched: $a, ($b1, $b2)") }

  //case.eval(FlatMap(Distinct(foo), Map(bar, baz)))







//
//  println( FlatMap_M(Any<Entity>(), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
//  println( FlatMap_M(Any<Map>(), Map_M(Any(), Any())).matches(FlatMap(foo, Map(foo, foo))) )
}