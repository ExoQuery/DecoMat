package io.decomat

fun <P: Pattern0<R>, R> case(pat: Pattern0<R>) = Then0(pat, {true})

class Then0<P: Pattern0<R>, R>(
  override val pat: Pattern0<R>,
  override val check: (R) -> Boolean
): Stage<Pattern0<R>, R> {
  inline fun thenIf(crossinline f: (R) -> Boolean) = Then0(pat) { r: R -> f(r) }
  inline fun thenIfThis(crossinline f: R.() -> (R) -> Boolean) = Then0(pat) { r: R -> f(r)(r) }
  inline fun <O> then(crossinline f: (R) -> O): Case<O, R> = StageCase(pat, check) { r: R -> f(r) }
  inline fun <O> thenThis(crossinline f: R.() -> O): Case<O, R> = StageCase(pat, check) { r: R -> r.f() }
}

fun <P: Pattern1<P1, R1, R>, P1: Pattern<R1>, R1, R> case(pat: Pattern1<P1, R1, R>) = Then1(pat, {true})

class Then1<P: Pattern1<P1, R1, R>, P1: Pattern<R1>, R1, R>(
  override val pat: Pattern1<P1, R1, R>,
  override val check: (R) -> Boolean
): Stage<Pattern1<P1, R1, R>, R> {
  inline fun <O> useComponents(r: R, f: (R1) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1) = pat.divideIntoComponentsAny(it)
      f(r1)
    } ?: notRightCls(r)

  inline fun thenIf(crossinline f: (R1) -> Boolean) = Then1(pat) { r: R -> useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.() -> (R1) -> Boolean) = Then1(pat) { r: R -> useComponents(r, f(r)) }
  inline fun <O> then(crossinline f: (R1) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.() -> (R1) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f(r)) }
}

fun <P: Pattern2<P1, P2, R1, R2, R>, P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then2(pat, {true})

class Then2<P: Pattern2<P1, P2, R1, R2, R>, P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  inline fun <O> useComponents(r: R, f: (R1, R2) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      f(r1, r2)
    } ?: notRightCls(r)

  inline fun thenIf(crossinline f: (R1, R2) -> Boolean) = Then2(pat) { r: R -> useComponents(r, f) }
  inline fun thenIfThis(crossinline f: R.() -> (R1, R2) -> Boolean) = Then2(pat) { r: R -> useComponents(r, f(r)) }
  inline fun <O> then(crossinline f: (R1, R2) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f) }
  inline fun <O> thenThis(crossinline f: R.() -> (R1, R2) -> O) = StageCase(pat, check) { r: R -> useComponents(r, f(r)) }
}
