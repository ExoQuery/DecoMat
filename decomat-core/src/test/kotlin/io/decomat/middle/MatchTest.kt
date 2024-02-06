package io.decomat.middle

import io.decomat.*
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@Suppress("DANGEROUS_CHARACTERS")
class MatchTest {
  val foo = Entity("foo")
  val bar = Entity("bar")
  val baz = Entity("baz")
  val waz = Entity("waz")
  val kaz = Entity("kaz")
  val idA = "AAA"
  val idB = "BBB"
  val idC = "CCC"

  @Test
  fun `flatMap(?, ?)`() {
    assertTrue(FlatMap[Is(), Is()].matches(FlatMap(foo, idA, bar)))
    assertTrue(FlatMap[Is(), Is()].matches(FlatMap(foo, idA, Map(foo, idB, Map(waz, idC, kaz)))))
  }

  infix fun <T> T.shouldBe(b: T): Unit = assertEquals(this, b)

  @Test
  fun `flatMap(?, ?) - match`() {
    on(FlatMap(foo, idA, bar)).match(
      case(FlatMap[Is(), Is(waz)]).then { a, m, b -> fail() },
      case(FlatMap[Is(), Is()]).then { a, m, b -> Res3(a, m, b) }
    ) shouldBe Res3(foo, idA, bar)

    assertTrue(FlatMap[Is(), Is()].matches(FlatMap(foo, idA, bar)))
    assertTrue(FlatMap[Is(), Is()].matches(FlatMap(foo, idA, Map(foo, idB, Map(waz, idC, kaz)))))
  }

  @Test
  fun `flatMap(?, Map(?))`() {
    on(FlatMap(foo, idA, Map(bar, idB, baz))).match(
      case(FlatMap[Is(), Map[Is(), Is(waz)]]).then { a, m, b -> fail() },
      case(FlatMap[Is(), Is()]).then { a, m, b -> Res3(a, m, b) }
    ) shouldBe Res3(foo, idA, Map(bar, idB, baz))

    assertTrue(
      FlatMap[Is(), Is()].matches(FlatMap(foo, idA, Map(bar, idB, baz)))
    )
  }

  @Test
  fun `flatMap(Map(?), ?)`() {
    on(FlatMap(Map(foo, idB, bar), idA, baz)).match(
      case(FlatMap[Map[Is(), Is(waz)], Is()]).then { a, m, b -> fail() },
      case(FlatMap[Is(), Is()]).then { a, m, b -> Res3(a, m, b) }
    ) shouldBe Res3(Map(foo, idB, bar), idA, baz)

    assertTrue(
      FlatMap[Is(), Is()].matches(FlatMap(foo, idA, Map(bar, idB, baz)))
    )
  }

  @Test
  fun `flatMap(Map(?), Map(?))`() {
    on(FlatMap(Map(foo, idA, bar), idB, Map(baz, idC, waz))).match(
      case(FlatMap[Map[Is(), Is(kaz)], Map[Is(), Is(kaz)]]).then { a, m, b -> fail() },
      case(FlatMap[Map[Is(), Is()], Map[Is(), Is()]]).then { (a1, am, a2), m, (b1, bm, b2) -> Res3(Res3(a1, am, a2), m, Res3(b1, bm, b2)) }
    ) shouldBe Res3(Res3(foo, idA, bar), idB, Res3(baz, idC, waz))

    assertTrue(
      FlatMap[Map[Is(), Is()], Map[Is(), Is()]].matches(FlatMap(Map(foo, idA, bar), idB, Map(baz, idC, waz)))
    )
  }

  @Test
  fun `flatMap(?, Map(Map))`() {
    on(FlatMap(foo, idA, Map(bar, idB, baz))).match(
      case(FlatMap[Is(), Map[Is(), Is(waz)]]).then { a, m, b -> fail() },
      case(FlatMap[Is(), Map[Is(), Is()]]).then { a, m, (b1, m1, b2) -> Res3(a, m, Res3(b1, m1, b2)) }
    ) shouldBe Res3(foo, idA, Res3(bar, idB, baz))

    assertTrue(
      FlatMap[Is(), Map[Is(), Is()]].matches(FlatMap(foo, idA, Map(foo, idB, Map(waz, idC, kaz))))
    )
  }

  @Test
  fun `flatMap(Map(Map), ?)`() {
    on(FlatMap(Map(foo, idA, bar), idB, baz)).match(
      case(FlatMap[Map[Is(), Is(waz)], Is()]).then { a, m, b -> fail() },
      case(FlatMap[Map[Is(), Is()], Is()]).then { (a1, m1, a2), m, b -> Res3(Res3(a1, m1, a2), m, b) }
    ) shouldBe Res3(Res3(foo, idA, bar), idB, baz)

    assertTrue(
      FlatMap[Map[Is(), Is()], Is()].matches(FlatMap(Map(foo, idA, bar), idB, baz))
    )
  }

  @Test
  fun `flatMap(Map(Map), Map(Map))`() {
    on(FlatMap(Map(foo, idA, bar), idB, Map(baz, idC, waz))).match(
      case(FlatMap[Map[Is(), Is(kaz)], Map[Is(), Is(kaz)]]).then { a, m, b -> fail() },
      case(FlatMap[Map[Is(), Is()], Map[Is(), Is()]]).then { (a1, m1, a2), m, (b1, m2, b2) -> Res3(Res3(a1, m1, a2), m, Res3(b1, m2, b2)) }
    ) shouldBe Res3(Res3(foo, idA, bar), idB, Res3(baz, idC, waz))

    assertTrue(
      FlatMap[Map[Is(), Is()], Map[Is(), Is()]].matches(FlatMap(Map(foo, idA, bar), idB, Map(baz, idC, waz)))
    )
  }

  @Test
  fun `flatMap(?, Map(!Entity) - Negative`() {
    assertFalse(
      FlatMap[Is(), Map[Is(), Is<Entity>()]].matches(FlatMap(foo, idA, Map(foo, idB, Map(waz, idC, kaz))))
    )
  }

  @Test
  fun `flatMap(?, Map(!Map) - Negative`() {
    assertFalse(
      FlatMap[Is(), Map[Is(), Is<Map>()]].matches(FlatMap(foo, idA, Map(foo, idB, foo)))
    )
  }

  @Test
  fun `flatMap(foo, Map(?))`() {
    assertTrue(
      FlatMap[Is(foo), Map[Is(), Is()]].matches(FlatMap(foo, idA, Map(foo, idB, foo)))
    )
  }

  @Test
  fun `flatMap(!foo, Map(?)) - Negative`() {
    assertFalse(
      FlatMap[Is(bar), Map[Is(), Is()]].matches(FlatMap(foo, idA, Map(foo, idB, foo)))
    )
  }

}