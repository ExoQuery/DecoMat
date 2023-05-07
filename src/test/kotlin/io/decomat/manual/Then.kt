package io.decomat.manual

import io.decomat.*

//fun <P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R, O> Pattern2<P1, P2, R1, R2, R>.thenThis(f: (R) -> O): O = TODO()
//fun <P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R> Pattern2<P1, P2, R1, R2, R>.thenIfThis(f: (R) -> Boolean): Boolean = TODO()
//
//fun <P1: Pattern<R1>, P2: Pattern<R2>, P3: Pattern<R3>, R1, R2, R3, R, O> Pattern3<P1, P2, P3, R1, R2, R3, R>.thenThis(f: (R) -> O): O = TODO()

// can use members of R as locals as well as the current context, just need to make sure to nest the lambdas
// TODO Check how K2 works with this





// no divideIntoComponents on Pattern0 since it doesn't make sense sin it's only the base-object, nothing is inside
// todo renmae a, b into p1/p2 or even longer pattern1, pattern2

//@JvmName("then00")
//fun <P1: Pattern0<R1>, P2: Pattern0<R2>, R1, R2, R, O>
//  Stage<Pattern2<P1, P2, R1, R2, R>, R>.then(f: (R1, R2) -> O): Case<O> =
//    object: Case<O> {
//      private val self = this@then
//
//      override fun matches(value: Any): Boolean =
//        (value as? ProductClass<*>)?.let { self.pat.matchesAny(it) && self.check(value as R) } ?: false
//
//      override fun eval(value: Any): O =
//        (value as? ProductClass<*>)?.let {
//          val (r1, r2) = self.pat.divideIntoComponentsAny(it)
//          f(r1, r2)
//        } ?: throw IllegalArgumentException("foo")
//    }




fun <P1: Pattern0<R1>, P2: Pattern0<R2>, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) =
  Then00<P1, P2, R1, R2, R>(pat, {true})

class Then00<P1: Pattern0<R1>, P2: Pattern0<R2>, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (R1, R2) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      f(r1, r2)
    } ?: throw IllegalArgumentException("foo")

  fun thenIf(f: (R1, R2) -> Boolean): Then00<P1, P2, R1, R2, R> =
    Then00<P1, P2, R1, R2, R>(pat) { r: R -> useComponents(r, f) }

  fun thenIfThis(f: R.() -> (R1, R2) -> Boolean): Then00<P1, P2, R1, R2, R> =
    Then00<P1, P2, R1, R2, R>(pat) { r: R -> useComponents(r, f(r)) }

  fun <O> then(f: (R1, R2) -> O): Case<O, R> =
    StageCase(pat, check) { value -> useComponents(value, f) }

  fun <O> thenThis(f: R.() -> (R1, R2) -> O): Case<O, R> =
    StageCase(pat, check) { v -> useComponents(v, f(v)) }
}




// TODO thenIfBoth
//fun <P1: Pattern0<R1>, P2: Pattern0<R2>, R1, R2, R, O>
//  Pattern2<P1, P2, R1, R2, R>.thenBoth(f: R.() -> (R1, R2) -> O): Case<O> =
//    object: Case<O> {
//      override fun matches(value: Any): Boolean =
//        (value as? ProductClass<*>)?.let { this@thenBoth.matchesAny(it) } ?: false
//
//      override fun eval(value: Any): O =
//        (value as? ProductClass<*>)?.let {
//          val (r1, r2) = this@thenBoth.divideIntoComponentsAny(it)
//          f(it as R)(r1, r2)
//        } ?: throw IllegalArgumentException("foo")
//    }
//
//
//
//
//@JvmName("then02")
//fun <P1: Pattern0<R1>, P2: Pattern2<P21, P22, R21, R22, R2>, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R, O>
//  Pattern2<P1, P2, R1, R2, R>.then(f: (R1, Components2<R21, R22>) -> O): Case<O> =
//  object: Case<O> {
//    override fun matches(value: Any): Boolean =
//      (value as? ProductClass<*>)?.let { this@then.matchesAny(it) } ?: false
//
//    override fun eval(value: Any): O =
//      (value as? ProductClass<*>)?.let {
//        val (r1, r2) = this@then.divideIntoComponentsAny(it)
//        val (r21, r22) = this@then.pattern2.divideIntoComponentsAny(r2 as Any)
//        f(r1, Components2(r21, r22))
//      } ?: throw IllegalArgumentException("foo")
//  }
//
//// TODO thenIf
//// Should there be a comp1?
//@JvmName("then12")
//fun <P1: Pattern1<P11, R11, R1>, B: Pattern2<P21, P22, R21, R22, R2>, P11: Pattern<R11>, R11, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R, O>
//  Pattern2<P1, B, R1, R2, R>.then(f: (Components1<R11>, Components2<R21, R22>) -> O): Case<O> =
//  object: Case<O> {
//    override fun matches(value: Any): Boolean =
//      (value as? ProductClass<*>)?.let { this@then.matchesAny(it) } ?: false
//
//    override fun eval(value: Any): O =
//      (value as? ProductClass<*>)?.let {
//        val (r1, r2) = this@then.divideIntoComponentsAny(it)
//        val (r11) = this@then.pattern1.divideIntoComponentsAny(r1 as Any)
//        val (r21, r22) = this@then.pattern2.divideIntoComponentsAny(r2 as Any)
//        f(Components1(r11), Components2(r21, r22))
//      } ?: throw IllegalArgumentException("foo")
//  }
//
//@JvmName("then22")
//fun <P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern2<P21, P22, R21, R22, R2>, P11: Pattern<R21>, P12: Pattern<R12>, R11, R12, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R, O>
//  Pattern2<P1, P2, R1, R2, R>.then(f: (Components2<R11, R12>, ProductClass2<R2, R21, R22>) -> O): Case<O> = TODO()
//
//
//
//
