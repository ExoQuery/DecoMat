package io.decomat

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
      assertTrue(FlatMap_M(Is(), Is()).matches(FlatMap(foo, bar)))
      assertTrue(FlatMap_M(Is(), Is()).matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))
    }

    @Test
    fun `flatMap(?, Map(?))`() =
      assertTrue(
        FlatMap_M(Is(), Is()).matches(FlatMap(foo, Map(bar, baz)))
      )

    @Test
    fun `flatMap(?, Map(Map)`() =
      assertTrue(
        FlatMap_M(Is(), Map_M(Is(), Is<Map>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz))))
      )

    @Test
    fun `flatMap(?, Map(!Entity) - Negative`() =
      assertFalse(
        FlatMap_M(Is(), Map_M(Is(), Is<Entity>())).matches(FlatMap(foo, Map(foo, Map(waz, kaz))))
      )

    @Test
    fun `flatMap(?, Map(!Map) - Negative`() =
      assertFalse(
        FlatMap_M(Is(), Map_M(Is(), Is<Map>())).matches(FlatMap(foo, Map(foo, foo)))
      )

    @Test
    fun `flatMap(foo, Map(?))`() =
      assertTrue(
        FlatMap_M(Is(foo), Map_M(Is(), Is())).matches(FlatMap(foo, Map(foo, foo)))
      )

    @Test
    fun `flatMap(!foo, Map(?)) - Negative`() =
      assertFalse(
        FlatMap_M(Is(bar), Map_M(Is(), Is())).matches(FlatMap(foo, Map(foo, foo)))
      )
  }

  @Nested
  inner class BracketSyntax {
    @Test
    fun `flatMap(?, Map(Map))`() =
      assertTrue(FlatMap[Is(), Map[Is(), Is<Map>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

    @Test
    fun `flatMap(?, Map(!Map))`() =
      assertFalse(FlatMap[Is(), Map[Is(), Is<Entity>()]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

    @Test
    fun `flatMap(?, Map(Is_Map))`() =
      assertTrue(FlatMap[Is(), Map[Is(), Is.Map]].matches(FlatMap(foo, Map(foo, Map(waz, kaz)))))

  }
}
