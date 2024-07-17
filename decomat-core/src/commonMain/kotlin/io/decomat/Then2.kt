package io.decomat

fun <R1, R2, R> case(pat: Pattern2<Pattern0<R1>, Pattern0<R2>, R1, R2, R>) = Then00(pat, {true})

class Then00<R1, R2, R>(
  override val pat: Pattern2<Pattern0<R1>, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern0<R1>, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, R2) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    //skip
    return f(r1, r2)
  }
  inline fun thenIf(crossinline f: (R1, R2) -> Boolean) =
    Then00(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, R2) -> Boolean) =
    Then00(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (R1, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R1, R21, R2, R> case(pat: Pattern2<Pattern0<R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then01(pat, {true})

class Then01<R1, R21, R2, R>(
  override val pat: Pattern2<Pattern0<R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern0<R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, Components1<R21>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(r1, Components1(r21))
  }
  inline fun thenIf(crossinline f: (R1, Components1<R21>) -> Boolean) =
    Then01(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, Components1<R21>) -> Boolean) =
    Then01(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (R1, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R1, R21, R22, R2, R> case(pat: Pattern2<Pattern0<R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then02(pat, {true})

class Then02<R1, R21, R22, R2, R>(
  override val pat: Pattern2<Pattern0<R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern0<R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, Components2<R21, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(r1, Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (R1, Components2<R21, R22>) -> Boolean) =
    Then02(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, Components2<R21, R22>) -> Boolean) =
    Then02(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (R1, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R1, R21, M2, R22, R2, R> case(pat: Pattern2<Pattern0<R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then03(pat, {true})

class Then03<R1, R21, M2, R22, R2, R>(
  override val pat: Pattern2<Pattern0<R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern0<R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, Components2M<R21, M2, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    //skip
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(r1, Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (R1, Components2M<R21, M2, R22>) -> Boolean) =
    Then03(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, Components2M<R21, M2, R22>) -> Boolean) =
    Then03(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (R1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R1, R2, R> case(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern0<R2>, R1, R2, R>) = Then10(pat, {true})

class Then10<R11, R1, R2, R>(
  override val pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, R2) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    //skip
    return f(Components1(r11), r2)
  }
  inline fun thenIf(crossinline f: (Components1<R11>, R2) -> Boolean) =
    Then10(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, R2) -> Boolean) =
    Then10(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R1, R21, R2, R> case(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then11(pat, {true})

class Then11<R11, R1, R21, R2, R>(
  override val pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, Components1<R21>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components1(r11), Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components1<R11>, Components1<R21>) -> Boolean) =
    Then11(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, Components1<R21>) -> Boolean) =
    Then11(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R1, R21, R22, R2, R> case(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then12(pat, {true})

class Then12<R11, R1, R21, R22, R2, R>(
  override val pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, Components2<R21, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components1(r11), Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (Components1<R11>, Components2<R21, R22>) -> Boolean) =
    Then12(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, Components2<R21, R22>) -> Boolean) =
    Then12(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R1, R21, M2, R22, R2, R> case(pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then13(pat, {true})

class Then13<R11, R1, R21, M2, R22, R2, R>(
  override val pat: Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern1<Pattern<R11>, R11, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, Components2M<R21, M2, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(Components1(r11), Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (Components1<R11>, Components2M<R21, M2, R22>) -> Boolean) =
    Then13(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, Components2M<R21, M2, R22>) -> Boolean) =
    Then13(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R12, R1, R2, R> case(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>) = Then20(pat, {true})

class Then20<R11, R12, R1, R2, R>(
  override val pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, R2) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    //skip
    return f(Components2(r11, r12), r2)
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, R2) -> Boolean) =
    Then20(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, R2) -> Boolean) =
    Then20(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R12, R1, R21, R2, R> case(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then21(pat, {true})

class Then21<R11, R12, R1, R21, R2, R>(
  override val pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, Components1<R21>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2(r11, r12), Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, Components1<R21>) -> Boolean) =
    Then21(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, Components1<R21>) -> Boolean) =
    Then21(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R12, R1, R21, R22, R2, R> case(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then22(pat, {true})

class Then22<R11, R12, R1, R21, R22, R2, R>(
  override val pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, Components2<R21, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2(r11, r12), Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, Components2<R21, R22>) -> Boolean) =
    Then22(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, Components2<R21, R22>) -> Boolean) =
    Then22(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, R12, R1, R21, M2, R22, R2, R> case(pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then23(pat, {true})

class Then23<R11, R12, R1, R21, M2, R22, R2, R>(
  override val pat: Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, Components2M<R21, M2, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(Components2(r11, r12), Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, Components2M<R21, M2, R22>) -> Boolean) =
    Then23(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, Components2M<R21, M2, R22>) -> Boolean) =
    Then23(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, M1, R12, R1, R2, R> case(pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>) = Then30(pat, {true})

class Then30<R11, M1, R12, R1, R2, R>(
  override val pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, R2) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    //skip
    return f(Components2M(r11, m1, r12), r2)
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, R2) -> Boolean) =
    Then30(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, R2) -> Boolean) =
    Then30(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, M1, R12, R1, R21, R2, R> case(pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then31(pat, {true})

class Then31<R11, M1, R12, R1, R21, R2, R>(
  override val pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, Components1<R21>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, Components1<R21>) -> Boolean) =
    Then31(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, Components1<R21>) -> Boolean) =
    Then31(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, M1, R12, R1, R21, R22, R2, R> case(pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then32(pat, {true})

class Then32<R11, M1, R12, R1, R21, R22, R2, R>(
  override val pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, Components2<R21, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, Components2<R21, R22>) -> Boolean) =
    Then32(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, Components2<R21, R22>) -> Boolean) =
    Then32(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}
fun <R11, M1, R12, R1, R21, M2, R22, R2, R> case(pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then33(pat, {true})

class Then33<R11, M1, R12, R1, R21, M2, R22, R2, R>(
  override val pat: Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, Components2M<R21, M2, R22>) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, Components2M<R21, M2, R22>) -> Boolean) =
    Then33(pat) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, Components2M<R21, M2, R22>) -> Boolean) =
    Then33(pat) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(c1, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, c2 -> f(v, c1, c2) }) }
}



fun <R1, M, R2, R> case(pat: Pattern2M<Pattern0<R1>, M, Pattern0<R2>, R1, R2, R>) = Then0M0(pat, {true})

class Then0M0<R1, M, R2, R>(
  override val pat: Pattern2M<Pattern0<R1>, M, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern0<R1>, M, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, M, R2) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    //skip
    //skip
    return f(r1, m, r2)
  }
  inline fun thenIf(crossinline f: (R1, M, R2) -> Boolean) =
    Then0M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, M, R2) -> Boolean) =
    Then0M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (R1, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R1, M, R21, R2, R> case(pat: Pattern2M<Pattern0<R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then0M1(pat, {true})

class Then0M1<R1, M, R21, R2, R>(
  override val pat: Pattern2M<Pattern0<R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern0<R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, M, Components1<R21>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    //skip
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(r1, m, Components1(r21))
  }
  inline fun thenIf(crossinline f: (R1, M, Components1<R21>) -> Boolean) =
    Then0M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, M, Components1<R21>) -> Boolean) =
    Then0M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (R1, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R1, M, R21, R22, R2, R> case(pat: Pattern2M<Pattern0<R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then0M2(pat, {true})

class Then0M2<R1, M, R21, R22, R2, R>(
  override val pat: Pattern2M<Pattern0<R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern0<R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, M, Components2<R21, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    //skip
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(r1, m, Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (R1, M, Components2<R21, R22>) -> Boolean) =
    Then0M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, M, Components2<R21, R22>) -> Boolean) =
    Then0M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (R1, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R1, M, R21, M2, R22, R2, R> case(pat: Pattern2M<Pattern0<R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then0M3(pat, {true})

class Then0M3<R1, M, R21, M2, R22, R2, R>(
  override val pat: Pattern2M<Pattern0<R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern0<R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (R1, M, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    //skip
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(r1, m, Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (R1, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then0M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(R1, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then0M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (R1, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(R1, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R1, M, R2, R> case(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern0<R2>, R1, R2, R>) = Then1M0(pat, {true})

class Then1M0<R11, R1, M, R2, R>(
  override val pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, M, R2) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    //skip
    return f(Components1(r11), m, r2)
  }
  inline fun thenIf(crossinline f: (Components1<R11>, M, R2) -> Boolean) =
    Then1M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, M, R2) -> Boolean) =
    Then1M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R1, M, R21, R2, R> case(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then1M1(pat, {true})

class Then1M1<R11, R1, M, R21, R2, R>(
  override val pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, M, Components1<R21>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components1(r11), m, Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components1<R11>, M, Components1<R21>) -> Boolean) =
    Then1M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, M, Components1<R21>) -> Boolean) =
    Then1M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R1, M, R21, R22, R2, R> case(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then1M2(pat, {true})

class Then1M2<R11, R1, M, R21, R22, R2, R>(
  override val pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, M, Components2<R21, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components1(r11), m, Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (Components1<R11>, M, Components2<R21, R22>) -> Boolean) =
    Then1M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, M, Components2<R21, R22>) -> Boolean) =
    Then1M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R1, M, R21, M2, R22, R2, R> case(pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then1M3(pat, {true})

class Then1M3<R11, R1, M, R21, M2, R22, R2, R>(
  override val pat: Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern1<Pattern<R11>, R11, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components1<R11>, M, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(Components1(r11), m, Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (Components1<R11>, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then1M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components1<R11>, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then1M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components1<R11>, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components1<R11>, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R12, R1, M, R2, R> case(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern0<R2>, R1, R2, R>) = Then2M0(pat, {true})

class Then2M0<R11, R12, R1, M, R2, R>(
  override val pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, M, R2) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    //skip
    return f(Components2(r11, r12), m, r2)
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, M, R2) -> Boolean) =
    Then2M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, M, R2) -> Boolean) =
    Then2M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R12, R1, M, R21, R2, R> case(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then2M1(pat, {true})

class Then2M1<R11, R12, R1, M, R21, R2, R>(
  override val pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, M, Components1<R21>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2(r11, r12), m, Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, M, Components1<R21>) -> Boolean) =
    Then2M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, M, Components1<R21>) -> Boolean) =
    Then2M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R12, R1, M, R21, R22, R2, R> case(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then2M2(pat, {true})

class Then2M2<R11, R12, R1, M, R21, R22, R2, R>(
  override val pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, M, Components2<R21, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2(r11, r12), m, Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, M, Components2<R21, R22>) -> Boolean) =
    Then2M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, M, Components2<R21, R22>) -> Boolean) =
    Then2M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, R12, R1, M, R21, M2, R22, R2, R> case(pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then2M3(pat, {true})

class Then2M3<R11, R12, R1, M, R21, M2, R22, R2, R>(
  override val pat: Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2<R11, R12>, M, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(Components2(r11, r12), m, Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (Components2<R11, R12>, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then2M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2<R11, R12>, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then2M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2<R11, R12>, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2<R11, R12>, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, M1, R12, R1, M, R2, R> case(pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern0<R2>, R1, R2, R>) = Then3M0(pat, {true})

class Then3M0<R11, M1, R12, R1, M, R2, R>(
  override val pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern0<R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern0<R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, M, R2) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    //skip
    return f(Components2M(r11, m1, r12), m, r2)
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M, R2) -> Boolean) =
    Then3M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M, R2) -> Boolean) =
    Then3M0(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, M, R2) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, M1, R12, R1, M, R21, R2, R> case(pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>) = Then3M1(pat, {true})

class Then3M1<R11, M1, R12, R1, M, R21, R2, R>(
  override val pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern1<Pattern<R21>, R21, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, M, Components1<R21>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), m, Components1(r21))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M, Components1<R21>) -> Boolean) =
    Then3M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M, Components1<R21>) -> Boolean) =
    Then3M1(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, M, Components1<R21>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, M1, R12, R1, M, R21, R22, R2, R> case(pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then3M2(pat, {true})

class Then3M2<R11, M1, R12, R1, M, R21, R22, R2, R>(
  override val pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern2<Pattern<R21>, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, M, Components2<R21, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), m, Components2(r21, r22))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M, Components2<R21, R22>) -> Boolean) =
    Then3M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M, Components2<R21, R22>) -> Boolean) =
    Then3M2(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, M, Components2<R21, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}

fun <R11, M1, R12, R1, M, R21, M2, R22, R2, R> case(pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>) = Then3M3(pat, {true})

class Then3M3<R11, M1, R12, R1, M, R21, M2, R22, R2, R>(
  override val pat: Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2M<Pattern2M<Pattern<R11>, M1, Pattern<R12>, R11, R12, R1>, M, Pattern2M<Pattern<R21>, M2, Pattern<R22>, R21, R22, R2>, R1, R2, R>, R> {

  inline fun <O> useComponents(r: R, f: (Components2M<R11, M1, R12>, M, Components2M<R21, M2, R22>) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    val (r11, m1, r12) = pat.pattern1.divideInto3ComponentsAny(r1 as Any)
    val (r21, m2, r22) = pat.pattern2.divideInto3ComponentsAny(r2 as Any)
    return f(Components2M(r11, m1, r12), m, Components2M(r21, m2, r22))
  }
  inline fun thenIf(crossinline f: (Components2M<R11, M1, R12>, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then3M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun thenIfThis(crossinline f: R.(Components2M<R11, M1, R12>, M, Components2M<R21, M2, R22>) -> Boolean) =
    Then3M3(pat) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: (Components2M<R11, M1, R12>, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(c1, m, c2) }) }

  inline fun <O> thenThis(crossinline f: R.(Components2M<R11, M1, R12>, M, Components2M<R21, M2, R22>) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { c1, m, c2 -> f(v, c1, m, c2) }) }
}


