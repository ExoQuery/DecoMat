package io.decomat

data class ContextComponents1<A>(val compA: A)
data class ContextComponents2<A, B>(val compA: A, val compB: B)
object ContextComponents {
  fun <A> of(a: A) = ContextComponents1(a)
  fun <A, B> of(a: A, b: B) = ContextComponents2(a, b)
}
