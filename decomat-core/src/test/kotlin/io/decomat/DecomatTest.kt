package io.decomat

interface DecomatTest {
  val foo get() = Entity("foo")
  val bar get() = Entity("bar")
  val baz get() = Entity("baz")
  val waz get() = Entity("waz")
  val kaz get() = Entity("kaz")
}

sealed interface Res
data class Res1<A>(val a: A): Res
data class Res2<A, B>(val a: A, val b: B): Res
data class Res3<A, B, C>(val a: A, val b: B, val c: C): Res
data class Res4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D): Res
data class Res5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E): Res
data class Res6<A, B, C, D, E, F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F): Res

