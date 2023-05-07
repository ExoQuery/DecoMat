package io.decomat

import io.decomat.manual.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ThenTest {
  val foo = Entity("foo")
  val bar = Entity("bar")
  val baz = Entity("baz")
  val waz = Entity("waz")
  val kaz = Entity("kaz")

  data class Res1<A>(val a: A)
  data class Res2<A, B>(val a: A, val b: B)
  data class Res3<A, B, C>(val a: A, val b: B, val c: C)
  data class Res4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

  // Just check that the types don't fail
  @Test
  fun typesTest() {
    case(FlatMap_M(Is(), Is())).then { a: Query, b: Query -> Res2(a, b) }
  }

  @Test
  fun `Then00 - flatMap(Is, Is) - Filter`() =
    assert(
      case(FlatMap_M(Is(), Is<Entity>())).then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)) == Res2(foo, bar)
    )

  @Test
  fun `Then00 - flatMap(Is, Is) - thenThis`() =
    assert(
      case(FlatMap_M(Is(), Is())).thenThis {{ a, b -> Res3(a, b, body) }}.eval(FlatMap(foo, bar)) == Res3(foo, bar, bar)
    )

  @Test
  fun `Then00 - flatMap(Is, Is)`() =
    assert(
      case(FlatMap_M(Is(), Is())).then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)) == Res2(foo, bar)
    )

  @Test
  fun `Then01 - flatMap(Is, Distinct)`() =
    assert(
      case(FlatMap_M(Is(), Distinct_M(Is()))).then { a, (b) -> Res2(a, b) }.eval(FlatMap(foo, Distinct(bar))) == Res2(foo, bar)
    )

  @Test
  fun `Then02 - flatMap(Is, Map)`() =
    assert(
      case(FlatMap_M(Is(), Map_M(Is(), Is()))).then { a, (b1, b2) -> Res3(a, b1, b2) }.eval(FlatMap(foo, Map(bar, baz))) == Res3(foo, bar, baz)
    )

  @Test
  fun `Then10 - flatMap(Distinct, Is)`() =
    assert(
      case(FlatMap_M(Distinct_M(Is()), Is())).then { (a), b -> Res2(a, b) }.eval(FlatMap(Distinct(foo), bar)) == Res2(foo, bar)
    )

  @Test
  fun `Then20 - flatMap(Map, Is)`() =
    assert(
      case(FlatMap_M(Map_M(Is(), Is()), Is())).then { (a1, a2), b -> Res3(a1, a2, b) }.eval(FlatMap(Map(foo, bar), baz)) == Res3(foo, bar, baz)
    )

  @Test
  fun `Then11 - flatMap(Distinct, Distinct)`() =
    assert(
      case(FlatMap_M(Distinct_M(Is()), Distinct_M(Is()))).then { (a), (b) -> Res2(a, b) }.eval(FlatMap(Distinct(foo), Distinct(bar))) == Res2(foo, bar)
    )

  @Test
  fun `Then21 - flatMap(Map, Distinct)`() =
    assert(
      case(FlatMap_M(Map_M(Is(), Is()), Distinct_M(Is()))).then { (a1, a2), (b) -> Res3(a1, a2, b) }.eval(FlatMap(Map(foo, bar), Distinct(baz))) == Res3(foo, bar, baz)
    )

  @Test
  fun `Then22 - flatMap(Map, Map)`() =
    assert(
      case(FlatMap_M(Map_M(Is(), Is()), Map_M(Is(), Is()))).then { (a1, a2), (b1, b2) -> Res4(a1, a2, b1, b2) }.eval(FlatMap(Map(foo, bar), Map(baz, waz))) == Res4(foo, bar, baz, waz)
    )
}
