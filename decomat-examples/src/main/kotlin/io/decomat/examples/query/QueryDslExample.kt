package io.decomat.examples.query

import io.decomat.*

object QueryDslExample {


  sealed interface Query

  @Matchable
  data class FlatMap(
    @Component val head: Query,
    val id: String,
    @Component val body: Query
  ): Query, HasProductClass<FlatMap> {
    override val productComponents = productComponentsOf(this, head, body)

    companion object {
    }
  }
  data class Entity(val name: String): Query


}