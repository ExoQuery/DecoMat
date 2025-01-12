package io.decomat

fun <R> case(pat: Pattern0<R>) = ThenIs(pat, {true})

data class PrematchCase(val prematch: Boolean) {
  inline operator fun <R> invoke(pat: Pattern0<R>) = ThenIs(pat, {prematch})
  inline operator fun <R1, R> invoke(pat: Pattern1<Pattern0<R1>, R1, R>) = Then0(pat, {prematch})
  inline operator fun <R11, R1, R> invoke(pat: Pattern1<Pattern1<Pattern<R11>, R11, R1>, R1, R>) = Then1(pat, {prematch})
  inline operator fun <R11, R12, R1, R> invoke(pat: Pattern1<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, R1, R>) = Then2(pat, {prematch})
  inline operator fun <R11, M1, R12, R1, R> invoke(pat: Pattern1<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, R1, R>) = Then2M(pat, {prematch})

  inline operator fun <R1, R2, R> invoke(pat: Pattern2<Pattern0<R1>, Pattern0<R2>, R1, R2, R>) = Then00(pat, {prematch})
  inline operator fun <R1, R21, R2, R> invoke(pat: Pattern2<Pattern0<R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then01(pat, {prematch})
  inline operator fun <R1, R21, R22, R2, R> invoke(pat: Pattern2<Pattern0<R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then02(pat, {prematch})
  inline operator fun <R1, M, R2, R> invoke(pat: Pattern2M<Pattern0<R1>, M, Pattern0<R2>, R1, R2, R>) = Then0M0(pat, {prematch})
  inline operator fun <R1, M, R21, R2, R> invoke(pat: Pattern2M<Pattern0<R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then0M1(pat, {prematch})
  inline operator fun <R1, M, R21, R22, R2, R> invoke(pat: Pattern2M<Pattern0<R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then0M2(pat, {prematch})
  // Then10
  inline operator fun <R11, R1, R2, R> invoke(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern0<R2>, R1, R2, R>) = Then10(pat, {prematch})
  inline operator fun <R11, R12, R1, R2, R> invoke(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern1<Pattern<R12>, R12, R2>, R1, R2, R>) = Then11(pat, {prematch})
  inline operator fun <R11, R1, R21, R22, R2, R> invoke(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then12(pat, {prematch})
  inline operator fun <R11, R1, M, R2, R> invoke(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern0<R2>, R1, R2, R>) = Then1M0(pat, {prematch})
  inline operator fun <R11, M1, R12, R1, R2, R> invoke(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M1, Pattern1<Pattern<R12>, R12, R2>, R1, R2, R>) = Then1M1(pat, {prematch})
  inline operator fun <R11, R1, M, R21, R22, R2, R> invoke(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then1M2(pat, {prematch})
  // Then20
  inline operator fun <R11, R12, R1, R2, R3, R> invoke(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>) = Then20(pat, {prematch})
  inline operator fun <R11, R12, R13, R1, R2, R3, R> invoke(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R13>, R13, R2>, R1, R2, R>) = Then21(pat, {prematch})
  inline operator fun <R11, R12, R1, M, R21, R22, R23, R2, R3, R> invoke(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then22(pat, {prematch})
  inline operator fun <R11, M1, R12, R1, R2, R> invoke(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M1, Pattern0<R2>, R1, R2, R>) = Then2M0(pat, {prematch})
  inline operator fun <R11, M1, R12, R13, R1, R2, R3, R> invoke(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M1, Pattern1<Pattern<R13>, R13, R2>, R1, R2, R>) = Then2M1(pat, {prematch})
  inline operator fun <R11, R12, R1, M, R21, R22, R23, R2, R3, R> invoke(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then2M2(pat, {prematch})
}

fun caseEarly(prematch: Boolean) = PrematchCase(prematch)

class ThenIs<R>(
  override val pat: Pattern0<R>,
  override val check: (R) -> Boolean
): Stage<Pattern0<R>, R> {
  inline fun <O> useComponents(r: R, f: (R) -> O): O {
    return f(r)
  }

  inline fun thenIf(crossinline f: (R) -> Boolean) = ThenIs(pat) { r: R -> check(r) && useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.(R) -> Boolean) = ThenIs(pat) { r: R -> check(r) && useComponents(r, { c -> f(r, c) }) }
  inline fun <O> then(crossinline f: (R) -> O): Case<O, R> = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.(R) -> O): Case<O, R> = StageCase(pat, check) { r: R -> useComponents(r, { c -> f(r, c) }) }
}



fun <R1, R> case(pat: Pattern1<Pattern0<R1>, R1, R>) = Then0(pat, {true})


class Then0<R1, R>(
  override val pat: Pattern1<Pattern0<R1>, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<Pattern0<R1>, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (R1) -> O): O {
    val (r1) = pat.divideIntoComponentsAny(r as Any)
    return f(r1)
  }

  inline fun thenIf(crossinline f: (R1) -> Boolean) = Then0(pat) { r: R -> check(r) && useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.(R1) -> Boolean) = Then0(pat) { r: R -> check(r) && useComponents(r, { c -> f(r, c) }) }
  inline fun <O> then(crossinline f: (R1) -> O): Case<O, R> = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.(R1) -> O): Case<O, R> = StageCase(pat, check) { r: R -> useComponents(r, { c -> f(r, c) }) }
}

fun <R11, R1, R> case(pat: Pattern1<Pattern1<Pattern<R11>, R11, R1>, R1, R>) = Then1(pat, {true})


class Then1<R11, R1, R>(
  override val pat: Pattern1<Pattern1<Pattern<R11>, R11, R1>, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<Pattern1<Pattern<R11>, R11, R1>, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (ContextComponents1<R1, R>, Components1<R11>) -> O): O {
    val (r1) = pat.divideIntoComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    return f(ContextComponents.of(r1, r), Components1(r11))
  }

  inline fun thenIf(crossinline f: (ContextComponents1<R1, R>).(Components1<R11>) -> Boolean) = Then1(pat) { r: R -> check(r) && useComponents(r, { cc, c -> with(cc) { f(cc, c) } }) }
  inline fun thenIfThis(crossinline f: R.(Components1<R11>) -> Boolean) = Then1(pat) { r: R -> check(r) && useComponents(r, { _, c -> f(r, c) }) }
  inline fun <O> then(crossinline f: (ContextComponents1<R1, R>).(Components1<R11>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, { cc, c -> f(cc, c) }) }
  inline fun <O> thenThis(crossinline f: R.(Components1<R11>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, { _, c -> f(r, c) }) }
}

/** Generic match for Pattern2<PatternA, PatternB> where we don't know what A and B are */
fun <R11, R12, R1, R> case(pat: Pattern1<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, R1, R>) = Then2(pat, {true})


class Then2<R11, R12, R1, R>(
  override val pat: Pattern1<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (ContextComponents1<R1, R>, Components2<R11, R12>) -> O): O {
    val (r1) = pat.divideIntoComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    return f(ContextComponents.of(r1, r), Components2(r11, r12))
  }

  inline fun thenIf(crossinline f: (ContextComponents1<R1, R>).(Components2<R11, R12>) -> Boolean) = Then2(pat) { r: R -> check(r) && useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>) -> Boolean) = Then2(pat) { r: R -> check(r) && useComponents(r, { _, c -> f(r, c) }) }
  inline fun <O> then(crossinline f: (ContextComponents1<R1, R>).(Components2<R11, R12>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, { _, c -> f(r, c) }) }
}

fun <R11, M1, R12, R1, R> case(pat: Pattern1<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, R1, R>) = Then2M(pat, {true})

class Then2M<R11, M1, R12, R1, R>(
  override val pat: Pattern1<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (ContextComponents1<R1, R>, Components2M<R11, M1, R12>) -> O): O {
    val (r1) = pat.divideIntoComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    return f(ContextComponents.of(r1, r), Components2M(r11, m1, r12))
  }

  inline fun thenIf(crossinline f: (ContextComponents1<R1, R>).(Components2M<R11, M1, R12>) -> Boolean) = Then2M(pat) { r: R -> check(r) && useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>) -> Boolean) = Then2M(pat) { r: R -> check(r) && useComponents(r, { _, c -> f(r, c) }) }
  inline fun <O> then(crossinline f: (ContextComponents1<R1, R>).(Components2M<R11, M1, R12>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>) -> O) = StageCase(pat, check) { r: R -> useComponents(r, { _, c -> f(r, c) }) }
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
