package io.decomat

interface DecomatTest {
  val foo get() = Entity("foo")
  val bar get() = Entity("bar")
  val baz get() = Entity("baz")
  val waz get() = Entity("waz")
  val kaz get() = Entity("kaz")
}

sealed interface Res
data class Res1<out A>(val a: A): Res
data class Res2<out A, out B>(val a: A, val b: B): Res
data class Res3<out A, out B, out C>(val a: A, val b: B, val c: C): Res
data class Res4<out A, out B, out C, out D>(val a: A, val b: B, val c: C, val d: D): Res
data class Res5<out A, out B, out C, out D, out E>(val a: A, val b: B, val c: C, val d: D, val e: E): Res
data class Res6<out A, out B, out C, out D, out E, out F>(val a: A, val b: B, val c: C, val d: D, val e: E, val f: F): Res

