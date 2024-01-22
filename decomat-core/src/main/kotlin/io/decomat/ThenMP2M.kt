package io.decomat

fun <P1: Pattern0<R1>, R1, M1, P2: Pattern2M<P21, M2, P22, R21, R22, R2>, P21: Pattern<R21>, R21, M2, P22: Pattern<R22>, R22, R2, R> case(pat: Pattern2M<P1, M1, P2, R1, R2, R>) = Then0MP2M(pat, {true})

class Then0MP2M<P1: Pattern0<R1>, R1, M1, P2: Pattern2M<P21, M2, P22, R21, R22, R2>, P21: Pattern<R21>, R21, M2, P22: Pattern<R22>, R22, R2, R>(
  override val pat: Pattern2M<P1, M1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<P1, M1, P2, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, M1, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r21, m2, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(r1, m1, Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (R1, M1, Components2M<R21, M2, R22>) -> Boolean) =
    Then0MP2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, M1, Components2M<R21, M2, R22>) -> Boolean) =
    Then0MP2M(pat) { v -> useComponents(v, { c1, m1, c2 -> f(v, c1, m1, c2) }) }

  inline fun <O> then(crossinline f: (R1, M1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, M1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <P1: Pattern1<P11, R11, R1>, P11: Pattern<R11>, R11, R1, M1, P2: Pattern2M<P21, M2, P22, R21, R22, R2>, P21: Pattern<R21>, R21, M2, P22: Pattern<R22>, R22, R2, R> case(pat: Pattern2M<P1, M1, P2, R1, R2, R>) = Then1MP2M(pat, {true})

class Then1MP2M<P1: Pattern1<P11, R11, R1>, P11: Pattern<R11>, R11, R1, M1, P2: Pattern2M<P21, M2, P22, R21, R22, R2>, P21: Pattern<R21>, R21, M2, P22: Pattern<R22>, R22, R2, R>(
  override val pat: Pattern2M<P1, M1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<P1, M1, P2, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, M1, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components1(r11), m1, Components2M(r21, m2, r22))
  }

  inline fun thenIf(crossinline f: (Components1<R11>, M1, Components2M<R21, M2, R22>) -> Boolean) =
    Then1MP2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, M1, Components2M<R21, M2, R22>) -> Boolean) =
    Then1MP2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, M1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }
}

fun <P1: Pattern2<P11, P12, R11, R12, R1>, P11: Pattern<R11>, R11, P12: Pattern<R12>, R12, R1, M1, P2: Pattern2M<P21, M2, P22, R21, R22, R2>, P21: Pattern<R21>, R21, M2, P22: Pattern<R22>, R22, R2, R> case(pat: Pattern2M<P1, M1, P2, R1, R2, R>) = Then2MP2M(pat, {true})

class Then2MP2M<P1: Pattern2<P11, P12, R11, R12, R1>, P11: Pattern<R11>, R11, P12: Pattern<R12>, R12, R1, M1, P2: Pattern2M<P21, M2, P22, R21, R22, R2>, P21: Pattern<R21>, R21, M2, P22: Pattern<R22>, R22, R2, R>(
  override val pat: Pattern2M<P1, M1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<P1, M1, P2, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, M1, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2(r11, r12), m1, Components2M(r21, m2, r22))
  }

  inline fun thenIf(crossinline f: (Components2<R11, R12>, M1, Components2M<R21, M2, R22>) -> Boolean) =
    Then2MP2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, M1, Components2M<R21, M2, R22>) -> Boolean) =
    Then2MP2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, M1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, M1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}



fun <P1: Pattern2M<P11, M1, P12, R11, R12, R1>, P11: Pattern<R11>, R11, M1, P12: Pattern<R12>, R12, R1, P2: Pattern0<R2>, R2, M2, R> case(pat: Pattern2M<P1, M2, P2, R1, R2, R>) = ThenM0P2M(pat, {true})

class ThenM0P2M<P1: Pattern2M<P11, M1, P12, R11, R12, R1>, P11: Pattern<R11>, R11, M1, P12: Pattern<R12>, R12, R1, P2: Pattern0<R2>, R2, M2, R>(
  override val pat: Pattern2M<P1, M2, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<P1, M2, P2, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R,  f: (Components2M<R11, M1, R12>, M2, R2) -> O): O {
    val (r1, m2, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r11, m1, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    return f(Components2M(r11, m1, r12), m2, r2)
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M2, R2) -> Boolean) =
    ThenM0P2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M2, R2) -> Boolean) =
    ThenM0P2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M2, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, M2, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}


fun <M1: Pattern2M<P11, M1, P12, R11, R12, R1>, P11: Pattern<R11>, R11, P12: Pattern<R12>, R12, R1, P2: Pattern1<P21, R21, R2>, P21: Pattern<R21>, R21, R2, M2, R> case(pat: Pattern2M<M1, M2, P2, R1, R2, R>) = ThenM1P2M(pat, {true})

class ThenM1P2M<P1: Pattern2M<P11, M1, P12, R11, R12, R1>, P11: Pattern<R11>, R11, M1, P12: Pattern<R12>, R12, R1, P2: Pattern1<P21, R21, R2>, P21: Pattern<R21>, R21, R2, M2, R>(
  override val pat: Pattern2M<P1, M2, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<P1, M2, P2, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R,  f: (Components2M<R11, M1, R12>, M2, Components1<R21>) -> O): O {
    val (r1, m2, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r11, m1, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), m2, Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M2, Components1<R21>) -> Boolean) =
    ThenM1P2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M2, Components1<R21>) -> Boolean) =
    ThenM1P2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M2, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }
}

class ThenM2P2M<P1: Pattern2M<P11, M1, P12, R11, R12, R1>, P11: Pattern<R11>, R11, M1, P12: Pattern<R12>, R12, R1, P2: Pattern2<P21, P22, R21, R22, R2>, P21: Pattern<R21>, R21, P22: Pattern<R22>, R22, R2, M2, R>(
  override val pat: Pattern2M<P1, M2, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<P1, M2, P2, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, M2, Components2<R21, R22>) -> O): O {
    val (r1, m2, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r11, m1, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), m2, Components2(r21, r22))
  }

  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M2, Components2<R21, R22>) -> Boolean) =
    ThenM2P2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M2, Components2<R21, R22>) -> Boolean) =
    ThenM2P2M(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M2, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, M2, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}
