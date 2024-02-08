package io.decomat

data class ContextComponents1<A, R>(val compInner: A, val comp: R)
data class ContextComponents1L<A, R>(val compLeft: A, val comp: R)
data class ContextComponents1R<A, R>(val compRight: A, val comp: R)
data class ContextComponents2<A, B, R>(val compLeft: A, val compRight: B, val comp: R)
object ContextComponents {
  fun <A, R> of(a: A, r: R) = ContextComponents1(a, r)
  fun <A, R> ofLeft(a: A, r: R) = ContextComponents1L(a, r)
  fun <A, R> ofRight(a: A, r: R) = ContextComponents1R(a, r)
  fun <A, B, R> of(a: A, b: B, r: R) = ContextComponents2(a, b, r)
}
