package io.decomat.examples.query

import io.decomat.*

//object QueryDslExample {
//  sealed interface Query<T>
//
//  @Matchable
//  data class FlatMap<T, R>(
//    @Component val head: Query<T>,
//    val id: String,
//    @Component val body: Query<R>
//  ): Query<R>, HasProductClass<FlatMap<T, R>> {
//    override val productComponents = productComponentsOf(this, head, body)
//
//    companion object {
//    }
//  }
//  data class Entity<T>(val name: String): Query<T>
//
//}


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

// TODO note to the user that ksp plugin is not good at matching things with generics
//      show the flatMap example of that.

// Defining it manually
@Matchable
data class Entity<T>(@Component val v: T): Query<T>, HasProductClass<Entity<T>> {
  override val productComponents = productComponentsOf(this, v)
  companion object {
  }
}

//class Entity_M<A: Pattern<AP>, AP: T, T>(a: A): Pattern1<A, AP, Entity<*>>(a, Typed<Entity<*>>())
//operator fun <A: Pattern<AP>, AP: T, T> Entity.Companion.get(a: A) = Entity_M(a)
//val Entity.Companion.Is get() = Is<Entity<*>>()



fun main() {
  val fm = FlatMap(Entity("foo"), "id", Entity(123))

  // Need to use this instead of `match` since the types are generic

  val out =
    on(fm).match(
      case(FlatMap[Entity[Is("foo")], Is<Entity<Int>>()]).then { (str), query -> Pair(str, query) }
    )

  println(out)
}


