package io.decomat.examples.querydslmiddle

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
  @MiddleComponent val id: String,
  @Component val body: Query<R>
): Query<R>, HasProductClass<FlatMap<T, R>> {
  override val productComponents = productComponentsOf(this, head, id, body)
  companion object {
  }
}

@Matchable
data class Entity<T>(@Component val v: T): Query<T>, HasProductClass<Entity<T>> {
  override val productComponents = productComponentsOf(this, v)
  companion object {
  }
}

/** Testing Custom Patterns */
// TODO Use these later
//
//object NestedM {
//  operator fun <AP: Pattern<A>, A: Query<*>> get(v: AP): Pattern1<AP, A, Nested<*>> =
//    customPattern1(v) { it: Nested<*> -> Components1(it.body) }
//}
//
//object FlatMapM {
//  operator fun <AP: Pattern<A>, BP: Pattern<B>, A: Query<*>, B: Query<*>> get(head: AP, body: BP): Pattern2M<AP, String, BP, A, B, FlatMap<*, *>> =
//    customPattern2M(head, body) { it: FlatMap<*, *> -> Components2M(it.head, it.id, it.body) }
//}
//
//object EntityM {
//  operator fun <AP: Pattern<A>, A> get(v: AP): Pattern1<AP, A, Entity<*>> =
//    customPattern1(v) { it: Entity<*> -> Components1(it.v) }
//}

// TODO This needs to be a test!
fun regularMatch() {
  val fm = FlatMap(Entity(123), "id", Entity("foo"))
  // Need to use this instead of `match` since the types are generic
  val out =
    on(fm as Query<*>).match(
      //case(FlatMap[Entity[Is(123)], Is<Entity<String>>()]).then { (str), middle, query -> Triple(str, middle, query) }
      case(FlatMap[Is(), Is()]).then { str, middle, query -> Triple(str, middle, query) }
    )
  println(out)
}

fun main() {
  regularMatch()
}
