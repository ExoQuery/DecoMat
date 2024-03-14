package io.decomat.examples

import io.decomat.*
import io.decomat.examples.querydsl.*
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("NAME_SHADOWING")
class QueryPatternTest {
  @Test
  fun flatMapRegular() {
    val fm = FlatMap(Entity(123), "id", Entity("foo"))
    val out =
      on(fm as Query<*>).match(
        case(FlatMap[Entity[Is(123)], Is<Entity<String>>()]).then { (str), query -> Pair(str, query) }
      )
    assertEquals(out, Pair(123, Entity("foo")))
  }

  @Test
  fun flatMapCustomm() {
    val fm = FlatMap(Entity(123), "id", Entity("foo"))
    val out =
      on(fm as Query<*>).match(
        case(FlatMapM[EntityM[Is(123)], Is<Entity<String>>()]).then { (str), query -> Pair(str, query) }
      )
    assertEquals(out, Pair(123, Entity("foo")))
  }

  @Test
  fun singleNestedRegular() {
    val ent = Nested(Entity(123))
    val wrongResult = Entity("wrong-result")
    val out =
      on(ent as Query<*>).match(
        case(NestedM[NestedM[Is<Entity<*>>()]])
          .then { ent -> wrongResult },
        case(Nested[Is<Entity<Int>>()])
          .then { ent -> ent }
      )
    assertEquals(Entity(123), out)
  }

  @Test
  fun doubleNestedRegular() {
    val ent = Nested(Nested(Entity(123)))
    val out =
      on(ent as Query<*>).match(
        case(NestedM[NestedM[Is<Entity<*>>()]])
          .then { (ent) -> ent }
      )
    assertEquals(out, Entity(123))
  }
}