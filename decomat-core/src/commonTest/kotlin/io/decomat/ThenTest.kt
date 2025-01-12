package io.decomat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

public class ThenTest: DecomatTest {
  // ThenIs - Is
  @Test
  fun thenIs_Is() =
    assertEquals(
      case(Is<Entity>()).then { a -> Res1(a) }.eval(foo), Res1(foo)
    )

  @Test
  fun thenIs_Is_Early() =
    assertEquals(
      caseEarly(false)(Is<Entity>()).then { a -> Res1(a) }.evalSafe(foo), null
    )

  @Test
  fun typesTest() {
    case(FlatMap_M(Is(), Is())).then { a: Query, b: Query -> Res2(a, b) }
  }

  // Then0 - distinct(Is) - Filter
  @Test
  fun then0_distinct_Is_Filter() =
    assertEquals<Res1<Entity>>(
      case(Distinct_M(Is<Entity>())).then { a -> Res1(a) }.eval(Distinct(foo)),  Res1(foo)
    )

  // Then0 - distinct(Is)
  @Test
  fun then0_distinct_Is() =
    assertEquals(
      case(Distinct_M(Is<Entity>())).then { a -> Res1(a) }.eval(Distinct(foo)), Res1(foo)
    )

  // Then1 - distinct(distinct(Is))
  @Test
  fun then1_distinct_distinct_Is() =
    assertEquals(
      case(Distinct_M(Distinct_M(Is<Entity>()))).then { (a) -> Res1(a) }.eval(Distinct(Distinct(foo))), Res1(foo)
    )

  // Then2 - distinct(flatMap(Is, Is))
  @Test
  fun then2_distinct_flatMap_Is_Is() =
    assertEquals(
      case(Distinct_M(FlatMap_M(Is(), Is()))).then { (a, b) -> Res2(a, b) }.eval(Distinct(FlatMap(foo, bar))), Res2(foo, bar)
    )

  // Then00 - flatMap(Is, Is) - Filter
  @Test
  fun then00_flatMap_Is_Is__Filter() =
    assertEquals(
      case(FlatMap_M(Is(), Is<Entity>())).then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)), Res2(foo, bar)
    )


  // Then00 - flatMap(Is, Is) - thenThis
  @Test
  fun then00_flatMap_Is_Is__thenThis() =
    assertEquals(
      case(FlatMap_M(Is(), Is())).thenThis { a, b -> Res3(a, b, body) }.eval(FlatMap(foo, bar)), Res3(foo, bar, bar)
    )

  // Then00 - flatMap(Is, Is)
  @Test
  fun then00_flatMap_Is_Is() =
    assertEquals(
      case(FlatMap_M(Is(), Is())).then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)), Res2(foo, bar)
    )

  // Then00-thenIf - flatMap(Is, Is)
  @Test
  fun then00_thenIf_flatMap_Is_Is() =
    assertEquals(
      case(FlatMap_M(Is(), Is())).thenIf { a, b -> a == foo && b == bar }.then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)), Res2(foo, bar)
    )

  // Then00-!thenIf - flatMap(Is, Is)
  @Test
  fun then00_not_thenIf_flatMap_Is_Is() =
    assertEquals(
      case(FlatMap_M(Is(), Is())).thenIf { a, b -> a != foo || b != bar }.then { a, b -> Res2(a, b) }.evalSafe(FlatMap(foo, bar)), null
    )

  // Then00-thenIfThis - flatMap(Is, Is)
  @Test
  fun then00_thenIfThis__flatMap_Is_Is() =
    assertEquals<Res2<Query, Query>>(
      case(FlatMap_M(Is(), Is())).thenIfThis { a, b ->
        a == foo && b == bar && this.head == foo && this.body == bar
      }.then { a, b -> Res2(a, b) }.eval(FlatMap(foo, bar)),
      // ==
      Res2(foo, bar)
    )

  // Then00-!thenIfThis - flatMap(Is, Is)
  @Test
  fun then00_not_thenIfThis__flatMap_Is_Is() =
    assertNull(
      case(FlatMap_M(Is(), Is())).thenIfThis { a, b ->
        a != foo || b != bar || this.head != foo || this.body != bar
      }.then { a, b -> Res2(a, b) }.evalSafe(FlatMap(foo, bar))
    )

  // Then01 - flatMap(Is, Distinct)
  @Test
  fun then01_flatMap_Is_Distinct() =
    assertEquals(
      case(FlatMap_M(Is(), Distinct_M(Is()))).then { a, (b) -> Res2(a, b) }.eval(FlatMap(foo, Distinct(bar))), Res2(foo, bar)
    )

  // Then02 - flatMap(Is, Map)
  @Test
  fun then02__flatMap_Is_Map() =
    assertEquals(
      case(FlatMap_M(Is(), Map_M(Is(), Is()))).then { a, (b1, b2) -> Res3(a, b1, b2) }.eval(FlatMap(foo, Map(bar, baz))), Res3(foo, bar, baz)
    )

  // Then10 - flatMap(Distinct, Is)
  @Test
  fun then10__flatMap_Distinct_Is() =
    assertEquals(
      case(FlatMap_M(Distinct_M(Is()), Is())).then { (a), b -> Res2(a, b) }.eval(FlatMap(Distinct(foo), bar)), Res2(foo, bar)
    )

  // Then11 - flatMap(Distinct, Distinct)
  @Test
  fun then11__flatMap_Distinct_Distinct() =
    assertEquals(
      case(FlatMap_M(Distinct_M(Is()), Distinct_M(Is()))).then { (a), (b) -> Res2(a, b) }.eval(FlatMap(Distinct(foo), Distinct(bar))), Res2(foo, bar)
    )

  // Then12 - flatMap(Distinct, Map)
  @Test
  fun then12__flatMap_Distinct_Map() =
    assertEquals(
      case(FlatMap_M(Distinct_M(Is()), Map_M(Is(), Is()))).then { (a), (b1, b2) -> Res3(a, b1, b2) }.eval(FlatMap(Distinct(foo), Map(bar, baz))), Res3(foo, bar, baz)
    )

  // Then20 - flatMap(Map, Is)
  @Test
  fun then20__flatMap_Map_Is() =
    assertEquals(
      case(FlatMap_M(Map_M(Is(), Is()), Is())).then { (a1, a2), b -> Res3(a1, a2, b) }.eval(FlatMap(Map(foo, bar), baz)), Res3(foo, bar, baz)
    )

  // Then21 - flatMap(Map, Distinct)
  @Test
  fun then21__flatMap_Map_Distinct() =
    assertEquals(
      case(FlatMap_M(Map_M(Is(), Is()), Distinct_M(Is()))).then { (a1, a2), (b) -> Res3(a1, a2, b) }.eval(FlatMap(Map(foo, bar), Distinct(baz))), Res3(foo, bar, baz)
    )

  // Then22 - flatMap(Map, Map)
  @Test
  fun then22__flatMap_Map_Map() =
    assertEquals(
      case(FlatMap_M(Map_M(Is(), Is()), Map_M(Is(), Is()))).then { (a1, a2), (b1, b2) -> Res4(a1, a2, b1, b2) }.eval(FlatMap(Map(foo, bar), Map(baz, waz))), Res4(foo, bar, baz, waz)
    )
}
