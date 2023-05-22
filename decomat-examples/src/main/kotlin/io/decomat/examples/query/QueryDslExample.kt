package io.decomat.examples.query

import io.decomat.*

object QueryDslExample {


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
  data class Entity<T>(val name: String): Query<T>


}