package io.decomat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

public class ThenTest: DecomatTest {
  // Just check that the types don't fail
  @Test
  fun `ThenIs - Is`() =
    assert(
      case(Is<Entity>()).then { a -> Res1(a) }.eval(foo) == Res1(foo)
    )

  @Test
  fun typesTest() {
    case(FlatMap_M(Is(), Is())).then { a: Query, b: Query -> Res2(a, b) }
  }

  @Test
  fun `Then0 - distinct(Is) - Filter`() =
    assertEquals<Res1<Entity>>(
      case(Distinct_M(Is<Entity>())).then { a -> Res1(a) }.eval(Distinct(foo)),  Res1(foo)
    )

  @Test
  fun `Then0 - distinct(Is)`() =
    assertEquals(
      case(Distinct_M(Is<Entity>())).then { a -> Res1(a) }.eval(Distinct(foo)), Res1(foo)
    )

  @Test
  fun `Then1 - distinct(distinct(Is))`() =
    assert(
      case(Distinct_M(Distinct_M(Is<Entity>()))).then { (a) -> Res1(a) }.eval(Distinct(Distinct(foo))) == Res1(foo)
    )

  @Test
  fun `Then1 - distinct(distinct(Is)) - thenThis`() =
    assert(
      case(Distinct_M(Distinct_M(Is<Entity>()))).then { _ -> Res1(compInner) }.eval(Distinct(Distinct(foo))) == Res1(Distinct(foo)) // conv
    )

  @Test
  fun `Then2 - distinct(flatMap(Is, Is))`() =
    assert(
      case(Distinct_M(FlatMap_M(Is(), Is()))).then { (a, b) -> Res2(a, b) }.eval(Distinct(FlatMap(foo, bar))) == Res2(foo, bar)
    )

  @Test
  fun `Then00 - flatMap(Is, Is) - Filter`() =
    assert(
      case(FlatMap_M(Is(), Is<Entity>())).then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)) == Res2(foo, bar)
    )

  @Test
  fun `Then00 - flatMap(Is, Is) - thenThis`() =
    assert(
      case(FlatMap_M(Is(), Is())).thenThis { a, b -> Res3(a, b, body) }.eval(FlatMap(foo, bar)) == Res3(foo, bar, bar)
    )

  @Test
  fun `Then00 - flatMap(Is, Is)`() =
    assert(
      case(FlatMap_M(Is(), Is())).then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)) == Res2(foo, bar)
    )

  @Test
  fun `Then00-thenIf - flatMap(Is, Is)`() =
    assert(
      case(FlatMap_M(Is(), Is())).thenIf { a, b -> a == foo && b == bar }.then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)) == Res2(foo, bar)
    )

  @Test
  fun `Then00-!thenIf - flatMap(Is, Is)`() =
    assert(
      case(FlatMap_M(Is(), Is())).thenIf { a, b -> a != foo || b != bar }.then { a, b -> Res2(a, b) }.evalSafe(FlatMap(foo, bar)) == null
    )

  @Test
  fun `Then00-thenIfThis - flatMap(Is, Is)`() =
    assertEquals<Res2<Query, Query>>(
      case(FlatMap_M(Is(), Is())).thenIfThis { a, b ->
        a == foo && b == bar && this.head == foo && this.body == bar
      }.then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)),
      // ==
      Res2(foo, bar)
    )

  @Test
  fun `Then00-!thenIfThis - flatMap(Is, Is)`() =
    assertNull(
      case(FlatMap_M(Is(), Is())).thenIfThis { a, b ->
        a != foo || b != bar || this.head != foo || this.body != bar
      }.then { a, b -> Res2(a, b) }.evalSafe(FlatMap(foo, bar))
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
  fun `Then11 - flatMap(Distinct, Distinct)`() =
    assert(
      case(FlatMap_M(Distinct_M(Is()), Distinct_M(Is()))).then { (a), (b) -> Res2(a, b) }.eval(FlatMap(Distinct(foo), Distinct(bar))) == Res2(foo, bar)
    )

  @Test
  fun `Then12 - flatMap(Distinct, Map)`() =
    assert(
      case(FlatMap_M(Distinct_M(Is()), Map_M(Is(), Is()))).then { (a), (b1, b2) -> Res3(a, b1, b2) }.eval(FlatMap(Distinct(foo), Map(bar, baz))) == Res3(foo, bar, baz)
    )

  @Test
  fun `Then20 - flatMap(Map, Is)`() =
    assert(
      case(FlatMap_M(Map_M(Is(), Is()), Is())).then { (a1, a2), b -> Res3(a1, a2, b) }.eval(FlatMap(Map(foo, bar), baz)) == Res3(foo, bar, baz)
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
