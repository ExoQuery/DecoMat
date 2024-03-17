package io.decomat

//typealias FunCC1C1<CC1, C1, O> = context(ContextComponents1<CC1>) (Components1<C1>) -> O
//typealias FunCC1C2<CC1, C1, C2, O> = context(ContextComponents1<CC1>) (Components2<C1, C2>) -> O
//typealias FunCC1C2M<CC1, C1, M, C2, O> = context(ContextComponents1<CC1>) (Components2M<C1, M, C2>) -> O
//typealias FunCC2C1<CC1, CC2, C1, O> = context(ContextComponents2<CC1, CC2>) (Components1<C1>) -> O
//typealias FunCC2C2<CC1, CC2, C1, C2, O> = context(ContextComponents2<CC1, CC2>) (Components2<C1, C2>) -> O
//typealias FunCC2C2M<CC1, CC2, C1, M, C2, O> = context(ContextComponents2<CC1, CC2>) (Components2M<C1, M, C2>) -> O

//class ContextFunctionMakerCC1C1<CC1, R, C1, O>(val r: R, val f: context(ContextComponents1<CC1>) R.(Components1<C1>) -> O) {
//  context(ContextComponents1<CC1>) operator fun invoke(c1: Components1<C1>): O = f(ContextComponents1(compA), r, c1)
//}
//class ContextFunctionMakerCC1C2<CC1, R, C1, C2, O>(val r: R, val f: context(ContextComponents1<CC1>) R.(Components2<C1, C2>) -> O) {
//  context(ContextComponents1<CC1>) operator fun invoke(c1: Components2<C1, C2>): O = f(ContextComponents1(compA), r, c1)
//}
//class ContextFunctionMakerCC1C2M<CC1, R, C1, M, C2, O>(val r: R, val f: context(ContextComponents1<CC1>) R.(Components2M<C1, M, C2>) -> O) {
//  context(ContextComponents1<CC1>) operator fun invoke(c1: Components2M<C1, M, C2>): O = f(ContextComponents1(compA), r, c1)
//}
//class ContextFunctionMakerCC2C1<CC1, CC2, R, C1, O>(val r: R, val f: context(ContextComponents2<CC1, CC2>) R.(Components1<C1>) -> O) {
//  context(ContextComponents2<CC1, CC2>) operator fun invoke(c1: Components1<C1>): O = f(ContextComponents2(compA, compB), r, c1)
//}
//class ContextFunctionMakerCC2C2<CC1, CC2, R, C1, C2, O>(val r: R, val f: context(ContextComponents2<CC1, CC2>) R.(Components2<C1, C2>) -> O) {
//  context(ContextComponents2<CC1, CC2>) operator fun invoke(c1: Components2<C1, C2>): O = f(ContextComponents2(compA, compB), r, c1)
//}
//class ContextFunctionMakerCC2C2M<CC1, CC2, R, C1, M, C2, O>(val r: R, val f: context(ContextComponents2<CC1, CC2>) R.(Components2M<C1, M, C2>) -> O) {
//  context(ContextComponents2<CC1, CC2>) operator fun invoke(c1: Components2M<C1, M, C2>): O = f(ContextComponents2(compA, compB), r, c1)
//}
//
//
////fun <CC1, C1, O> input(f: context(ContextComponents1<CC1>) (Components1<C1>) -> O) = f
//
//@JvmName("convertCC1C1")
//fun <CC1, C1, R, O> convert(f: context(ContextComponents1<CC1>) R.(Components1<C1>) -> O):
//    (R) -> context(ContextComponents1<CC1>) (Components1<C1>) -> O =
//  { r: R -> ContextFunctionMakerCC1C1(r, f)::invoke }
//
//@JvmName("convertCC1C2")
//fun <CC1, C1, C2, R, O> convert(f: context(ContextComponents1<CC1>) R.(Components2<C1, C2>) -> O):
//    (R) -> context(ContextComponents1<CC1>) (Components2<C1, C2>) -> O =
//  { r: R -> ContextFunctionMakerCC1C2(r, f)::invoke }
//
//@JvmName("convertCC2C1")
//fun <CC1, CC2, C1, R, O> convert( f: context(ContextComponents2<CC1, CC2>) R.(Components1<C1>) -> O):
//    (R) -> context(ContextComponents2<CC1, CC2>) (Components1<C1>) -> O =
//  { r: R -> ContextFunctionMakerCC2C1(r, f)::invoke }
//
//@JvmName("convertCC2C2")
//fun <CC1, CC2, C1, C2, R, O> convert( f: context(ContextComponents2<CC1, CC2>) R.(Components2<C1, C2>) -> O):
//    (R) -> context(ContextComponents2<CC1, CC2>) (Components2<C1, C2>) -> O =
//  { r: R -> ContextFunctionMakerCC2C2(r, f)::invoke }
//
//@JvmName("convertCC1C2M")
//fun <CC1, C1, M, C2, R, O> convert( f: context(ContextComponents1<CC1>) R.(Components2M<C1, M, C2>) -> O):
//    (R) -> context(ContextComponents1<CC1>) (Components2M<C1, M, C2>) -> O =
//  { r: R -> ContextFunctionMakerCC1C2M(r, f)::invoke }
//
//@JvmName("convertCC2C2M")
//fun <CC1, CC2, C1, M, C2, R, O> convert( f: context(ContextComponents2<CC1, CC2>) R.(Components2M<C1, M, C2>) -> O):
//    (R) -> context(ContextComponents2<CC1, CC2>) (Components2M<C1, M, C2>) -> O =
//  { r: R -> ContextFunctionMakerCC2C2M(r, f)::invoke }



@JvmName("convertCC1C1")
inline fun <CC1, C1, R, O> convert(crossinline f: context(ContextComponents1<CC1>) R.(Components1<C1>) -> O):
    (R) -> context(ContextComponents1<CC1>) (Components1<C1>) -> O =
  { r: R -> { c: Components1<C1> -> f(ContextComponents1(compA), r, c) } }

@JvmName("convertCC1C2")
inline fun <CC1, C1, C2, R, O> convert(crossinline f: context(ContextComponents1<CC1>) R.(Components2<C1, C2>) -> O):
    (R) -> context(ContextComponents1<CC1>) (Components2<C1, C2>) -> O =
  { r: R -> { c: Components2<C1, C2> -> f(ContextComponents1(compA), r, c) } }

@JvmName("convertCC2C1")
inline fun <CC1, CC2, C1, R, O> convert(crossinline f: context(ContextComponents2<CC1, CC2>) R.(Components1<C1>) -> O):
    (R) -> context(ContextComponents2<CC1, CC2>) (Components1<C1>) -> O =
  { r: R -> { c: Components1<C1> -> f(ContextComponents2(compA, compB), r, c) } }

@JvmName("convertCC2C2")
inline fun <CC1, CC2, C1, C2, R, O> convert(crossinline f: context(ContextComponents2<CC1, CC2>) R.(Components2<C1, C2>) -> O):
    (R) -> context(ContextComponents2<CC1, CC2>) (Components2<C1, C2>) -> O =
  { r: R -> { c: Components2<C1, C2> -> f(ContextComponents2(compA, compB), r, c) } }

@JvmName("convertCC1C2M")
inline fun <CC1, C1, M, C2, R, O> convert(crossinline f: context(ContextComponents1<CC1>) R.(Components2M<C1, M, C2>) -> O):
    (R) -> context(ContextComponents1<CC1>) (Components2M<C1, M, C2>) -> O =
  { r: R -> { c: Components2M<C1, M, C2> -> f(ContextComponents1(compA), r, c) } }

@JvmName("convertCC2C2M")
inline fun <CC1, CC2, C1, M, C2, R, O> convert(crossinline f: context(ContextComponents2<CC1, CC2>) R.(Components2M<C1, M, C2>) -> O):
    (R) -> context(ContextComponents2<CC1, CC2>) (Components2M<C1, M, C2>) -> O =
  { r: R -> { c: Components2M<C1, M, C2> -> f(ContextComponents2(compA, compB), r, c) } }




//class Foo {
//  val vvvv: String = TODO()
//}
//class FooFoo
//fun bar(): (FooFoo) -> context(Foo) (String) -> Int =
//  { ff: FooFoo -> { str: String -> vvvv.length } }
//
//
//
//fun <C, T, R> cc(f: C.(T) -> R): context(C) (T) -> R = f
//
//fun main() {
//  //println(cc<String, String, Int> { this.length }.invoke("foo", "bar"))
//  //bar { foofoo -> { str -> str.length } }
//}
//
//
//
//
