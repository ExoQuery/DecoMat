package io.decomat.examples.querydsl

import io.decomat.*

sealed interface Query<T>

@Matchable
data class Nested<T>(@Component val body: Query<T>): Query<T>, HasProductClass<Nested<T>> {
  override val productComponents = productComponentsOf(this, body)
  companion object {
  }
}

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

object NestedM {
  operator fun <AP: Pattern<A>, A: Query<*>> get(v: AP): Pattern1<AP, A, Nested<*>> =
    customPattern1(v) { it: Nested<*> -> Components1(it.body) }
}

object FlatMapM {
  operator fun <AP: Pattern<A>, BP: Pattern<B>, A: Query<*>, B: Query<*>> get(head: AP, body: BP): Pattern2<AP, BP, A, B, FlatMap<*, *>> =
    customPattern2(head, body) { it: FlatMap<*, *> -> Components2(it.head, it.body) }
}

object EntityM {
  operator fun <AP: Pattern<A>, A> get(v: AP): Pattern1<AP, A, Entity<*>> =
    customPattern1(v) { it: Entity<*> -> Components1(it.v) }
}

fun regularMatch() {
  val fm = FlatMap(Entity(123), "id", Entity("foo"))
  // Need to use this instead of `match` since the types are generic
  val out =
    on(fm as Query<*>).match(
      case(FlatMap[Entity[Is(123)], Is<Entity<String>>()]).then { (str), query -> Pair(str, query) }
    )
  println(out)
}

fun customMatch() {
  val fm = FlatMap(Entity(123), "id", Entity("foo"))
  // Need to use this instead of `match` since the types are generic
  val out2 =
    on(fm as Query<*>).match(
      case(FlatMapM[EntityM[Is(123)], Is<Entity<String>>()]).then { (str), query -> Pair(str, query) }
    )
  println(out2)
}

fun main() {
  regularMatch()
  customMatch()
}
