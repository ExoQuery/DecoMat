package io.decomat.middle

import io.decomat.*

/*
For some reason here I used the pattern where single letters A, B etc... are the pattern
vars and the AP, BP are the pattern atom as opposed to the generated code where
AP, BP are the pattern vars and A, B are the atoms
 */

sealed interface Query
data class FlatMap(val head: Query, val middle: String, val body: Query): Query, HasProductClass<FlatMap> {
  override val productComponents = ProductClass2M(this, head, middle, body)

  // Needed for static extension FlatMap_M
  companion object {
    // important for a, b to be A, B instead of just Pattern<A>, Pattern<B> because
    // the resolution to the correct `case` function doesn't know which case to choose
    // since case-functions are defined on specific Pattern subclasses e.g. case(P: Pattern3<...>)
    // all of them start to apply as soon as you generalize to Pattern
    operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> get(a: A, b: B): Pattern2M<A, String, B, AP, BP, FlatMap> = FlatMap_M(a, b)
  }
}

operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> FlatMap.Companion.get(a: A, b: B) = FlatMap_M(a, b)
class FlatMap_M<A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query>(a: A, b: B): Pattern2M<A, String, B, AP, BP, FlatMap>(a, b, Typed<FlatMap>())

data class Map(val head: Query, val middle: String, val body: Query): Query, HasProductClass<Map> {
  override val productComponents = ProductClass2M(this, head, middle, body)

  companion object {
    operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> get(a: Pattern<AP>, b: Pattern<BP>) = Map_M(a, b)
  }
}

operator fun <A: Pattern<AP>, B: Pattern<BP>, AP: Query, BP: Query> Map.Companion.get(a: A, b: B) = Map_M(a, b)
val Is.Companion.Map get() = Is<Map>()

class Map_M<P1: Pattern<A1>, P2: Pattern<A2>, A1: Query, A2: Query>(a: P1, b: P2): Pattern2M<P1, String, P2, A1, A2, Map>(a, b, Typed<Map>())

data class Distinct(val body: Query): Query, HasProductClass<Distinct> {
  override val productComponents get() = ProductClass1(this, body)
  companion object {
    operator fun <A: Pattern<AP>, AP: Query> get(a: Pattern<AP>) = Distinct_M(a)
  }
}

class Distinct_M<P: Pattern<A>, A: Query>(a: P): Pattern1<P, A, Distinct>(a, Typed<Distinct>())

operator fun <A: Pattern<AP>, AP: Query> Distinct.Companion.get(a: A) = Distinct_M(a)

data class Entity(val name: String): Query



