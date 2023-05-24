package io.decomat.examples.querydsl

import io.decomat.*

sealed interface Query<T>

@Matchable
data class FlatMap<T, R>(
  @Component val head: Query<T>,
  val id: String,
  @Component val body: Query<R>
): Query<R>, HasProductClass<FlatMap<T, R>> {
  override val productComponents = productComponentsOf(this, head, body)
  companion object {
  }
}

// Defining it manually
@Matchable
data class Entity<T>(@Component val v: T): Query<T>, HasProductClass<Entity<T>> {
  override val productComponents = productComponentsOf(this, v)
  companion object {
  }
}

fun main() {
  val fm = FlatMap(Entity("foo"), "id", Entity(123))

  // Need to use this instead of `match` since the types are generic

  val out =
    on(fm).match(
      case(FlatMap[Entity[Is("foo")], Is<Entity<Int>>()]).then { (str), query -> Pair(str, query) }
    )

  println(out)
}


