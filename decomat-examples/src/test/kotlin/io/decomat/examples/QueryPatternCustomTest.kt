package io.decomat.examples

import io.decomat.*
import io.decomat.examples.querydslcustom.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Very important to test situation with custom AST types where no ProductClass
 * for the AST type actually exists.
 */
class QueryPatternCustomTest {
  @Test
  fun flatMapCustomm() {
    val fm = FlatMap(Entity(123), "id", Entity("foo"))
    val out =
      on(fm as Query<*>).match( //
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
          .then { (ent) -> wrongResult },
        case(NestedM[Is<Entity<*>>()])
          .then { (ent) -> ent } //
      )
    assertEquals(out, Entity(123))
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