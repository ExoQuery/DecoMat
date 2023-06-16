package io.decomat

fun <P1: Pattern0<R1>, R1, R> case(pat: Pattern1<P1, R1, R>) = Then0(pat, {true})

class Then0<P1: Pattern0<R1>, R1, R>(
  override val pat: Pattern1<P1, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<P1, R1, R>, R> {
  inline fun thenIf(crossinline f: (R) -> Boolean) = Then0(pat) { r: R -> f(r) }
  inline fun thenIfThis(crossinline f: R.() -> (R) -> Boolean) = Then0(pat) { r: R -> f(r)(r) }
  inline fun <O> then(crossinline f: (R) -> O): Case<O, R> = StageCase(pat, check) { r: R -> f(r) }
  inline fun <O> thenThis(crossinline f: R.() -> O): Case<O, R> = StageCase(pat, check) { r: R -> r.f() }
}

fun <P1: Pattern1<P11, R11, R1>, P11: Pattern<R11>, R11, R1, R> case(pat: Pattern1<P1, R1, R>) = Then1(pat, {true})

class Then1<P1: Pattern1<P11, R11, R1>, P11: Pattern<R11>, R11, R1, R>(
  override val pat: Pattern1<P1, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<P1, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (Components1<R11>) -> O): O {
    val (r1) = pat.divideIntoComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    return f(Components1(r11))
  }

  inline fun thenIf(crossinline f: (Components1<R11>) -> Boolean) = Then1(pat) { r: R -> useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.() -> (Components1<R11>) -> Boolean) = Then1(pat) { r: R -> useComponents(r, f(r)) }
  inline fun <O> then(crossinline f: (Components1<R11>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.() -> (Components1<R11>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f(r)) }
}

/** Generic match for Pattern2<PatternA, PatternB> where we don't know what A and B are */
fun <P1: Pattern2<P11, P12, R11, R12, R1>, P11: Pattern<R11>, R11, P12: Pattern<R12>, R12, R1, R> case(pat: Pattern1<P1, R1, R>) = Then2(pat, {true})
//fun <P: Pattern2<P1, P2, R1, R2, R>, P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then2(pat, {true})

//class Then2<P: Pattern2<P1, P2, R1, R2, R>, P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R>(
class Then2<P1: Pattern2<P11, P12, R11, R12, R1>, P11: Pattern<R11>, R11, P12: Pattern<R12>, R12, R1, R>(
  override val pat: Pattern1<P1, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<P1, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>) -> O): O {
    val (r1) = pat.divideIntoComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    return f(Components2(r11, r12))
  }

  inline fun thenIf(crossinline f: (Components2<R11, R12>) -> Boolean) = Then2(pat) { r: R -> useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.() -> (Components2<R11, R12>) -> Boolean) = Then2(pat) { r: R -> useComponents(r, f(r)) }
  inline fun <O> then(crossinline f: (Components2<R11, R12>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.() -> (Components2<R11, R12>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f(r)) }
}

//fun <P: Pattern2<P1, P2, R1, R2, R>, P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = GenericThen2X(pat, {true})
//class GenericThen2X<P: Pattern2<P1, P2, R1, R2, R>, P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R>(
//  override val pat: Pattern2<P1, P2, R1, R2, R>,
//  override val check: (R) -> Boolean
//): Stage<Pattern2<P1, P2, R1, R2, R>, R> {
//  inline fun <O> useComponents(r: R, f: (R1, R2) -> O): O =
//    (r as? ProductClass<*>)?.let {
//      val (r1, r2) = pat.divideIntoComponentsAny(it)
//      f(r1, r2)
//    } ?: notRightCls(r)
//
//  inline fun thenIf(crossinline f: (R1, R2) -> Boolean) = GenericThen2(pat) { r: R -> useComponents(r, f) }
//  inline fun thenIfThis(crossinline f: R.() -> (R1, R2) -> Boolean) = GenericThen2(pat) { r: R -> useComponents(r, f(r)) }
//  inline fun <O> then(crossinline f: (R1, R2) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
//  inline fun <O> thenThis(crossinline f: R.() -> (R1, R2) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f(r)) }
//}
