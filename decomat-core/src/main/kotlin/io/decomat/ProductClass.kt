package io.decomat

sealed interface ProductClass<T> {
  val value: T
  fun isIfHas() =
    when(val thisComp = this) {
      is HasProductClass<T> -> thisComp.productComponents
      else -> this
    }
}

interface HasProductClass<T>: ProductClass<T> {
  val productComponents: ProductClass<T>
  override val value get() = productComponents.value
}

fun <T> productComponentsOf(host: T) = ProductClass0(host)
fun <T, A> productComponentsOf(host: T, componentA: A) = ProductClass1(host, componentA)
fun <T, A, B> productComponentsOf(host: T, componentA: A, componentB: B) = ProductClass2(host, componentA, componentB)
fun <T, A, B, C> productComponentsOf(host: T, componentA: A, componentB: B, componentC: C) = ProductClass3(host, componentA, componentB, componentC)

data class ProductClass0<T>(override val value: T): ProductClass<T>
data class ProductClass1<T, A>(override val value: T, val a: A): ProductClass<T>
data class ProductClass2<T, A, B>(override val value: T, val a: A, val b: B): ProductClass<T> {
  val matchComp get(): Components2<A, B> = Components2(a, b)
}

// For example: data class FlatMap(head: Query, body: Query) extends Comp3<FlatMap, Query, Query>
data class ProductClass3<T, A, B, C>(override val value: T, val a: A, val b: B, val c: C): ProductClass<T>


sealed interface Components
data class Components1<A>(val a: A): Components
// For example: data class FlatMap(head: Query, body: Query) extends Comp2<Query, Query>
data class Components2<A, B>(val a: A, val b: B): Components
data class Components3<A, B, C>(val a: A, val b: B, val c: C): Components