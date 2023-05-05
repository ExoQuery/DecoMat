package io.decomat

// TODO how to create a custom pattern that can be used to have user-defined logic
//      i.e. equivalent of a custom `unapply` implmentation

// FlatMap(IsAny, Any<Map>).then { a, (b, c) -> ... }
// FlatMap_M(IsAny, Map_M(IsAny, IsAny, Any<Map>)).then { a, (b, c) -> ... }
// of Map.Any

sealed interface Query
data class FlatMap(val head: Query, val body: Query): Query, HasProductClass<FlatMap> {
  override val productComponents = ProductClass2(this, head, body)

  // Needed for static extension FlatMap_M
  companion object {
  }
}

operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> FlatMap.Companion.get(a: A, b: B) = FlatMap_M(a, b)

class FlatMap_M<A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query>(a: A, b: B): Pattern2<A, B, AP, BP, FlatMap>(a, b, Typed<FlatMap>())
//class FlatMap_Is: Any<FlatMap>(Typed<FlatMap>())

data class Map(val head: Query, val body: Query): Query, HasProductClass<Map> {
  override val productComponents = ProductClass2(this, head, body)

  companion object {
  }
}

operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> Map.Companion.get(a: A, b: B) = Map_M(a, b)
val Is.Companion.Map get() = Is<Map>()

class Map_M<P1: Pattern<A1>, P2: Pattern<A2>, A1: Query, A2: Query>(a: P1, b: P2): Pattern2<P1, P2, A1, A2, Map>(a, b, Typed<Map>())
//class Map_Is: Any<Map>(Typed<Map>())

data class Distinct(val body: Query): Query, HasProductClass<Distinct> {
  override val productComponents get() = ProductClass1(this, body)
  companion object {
  }
}
class Distinct_M<P: Pattern<A>, A: Query>(a: P): Pattern1<P, A, Distinct>(a, Typed<Distinct>())

operator fun <A: Pattern<AP>, AP: Query> Distinct.Companion.get(a: A) = Distinct_M(a)


data class Entity(val name: String): Query
