package io.decomat

import kotlin.js.JsName
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


  /* ---------------------------- flatMap(?,Map(Map)) ---------------------------- */

  // flatMap(?, ?)
  @Test
  fun flatMap_QxQ() {
    assertTrue(FlatMap_M(Is(), Is()).matches(FlatMap(foo, bar)))
    assertTrue(FlatMap_M(Is(), Is()).matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))
  }

  // flatMap(?, Map(?))
  @Test
  fun flatMap_QxMapQ() =
    assertTrue(
      FlatMap_M(Is(), Is()).matches(FlatMap(foo, Map(bar, baz)))
    )

  // flatMap(?, Map(Map))
  @Test
  fun flatMap_QxMapMap() =
    assertTrue(
      FlatMap_M(Is(), Map_M(Is(), Is<Map>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz))))
    )

  // flatMap(?, Map(!Entity) - Negative
  @Test
  fun flatMap_QxMapEntity_Negative() =
    assertFalse(
      FlatMap_M(Is(), Map_M(Is(), Is<Entity>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz))))
    )

  // flatMap(?, Map(!Map)) - Negative
  @Test
  fun flatMap_QxMapNotMap_Negative() =
    assertFalse(
      FlatMap_M(Is(), Map_M(Is(), Is<Map>())).matches(FlatMap(foo, Map(foo, foo)))
    )

  // flatMap(foo, Map(?))
  @Test
  fun flatMap_FOOxMapQ() =
    assertTrue(
      FlatMap_M(Is(foo), Map_M(Is(), Is())).matches(FlatMap(foo, Map(foo, foo)))
    )

  // flatMap(!foo, Map(?)) - Negative
  @Test
  fun flatMap_NotFOOxMapQ_Negative() =
    assertFalse(
      FlatMap_M(Is(bar), Map_M(Is(), Is())).matches(FlatMap(foo, Map(foo, foo)))
    )

  /* ---------------------------- BracketSyntax ---------------------------- */
  // flatMap(?, Map(Map))
  @Test
  fun flatMap_QxMapMap2() =
    assertTrue(FlatMap[Is(), Map[Is(), Is<Map>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

  // flatMap(?, Map(!Map))
  @Test
  fun flatMap_QxMapNotMap() =
    assertFalse(FlatMap[Is(), Map[Is(), Is<Entity>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

  // flatMap(?, Map(Is_Map))
  @Test
  fun flatMap_QxMapIsMap() =
    assertTrue(FlatMap[Is(), Map[Is(), Is.Map]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))
}
