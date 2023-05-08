package io.decomat

fun <P1: Pattern0<R1>, P2: Pattern0<R2>, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then00(pat, {true})

class Then00<P1: Pattern0<R1>, P2: Pattern0<R2>, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
): Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (R1, R2) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      f(r1, r2)
    } ?: notRightCls(r)

  fun thenIf(f: (R1, R2) -> Boolean) = Then00(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (R1, R2) -> Boolean) = Then00(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (R1, R2) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (R1, R2) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern0<R1>, P2: Pattern1<P21, R21, R2>, R1, P21: Pattern<R21>, R21, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then01(pat, {true})

class Then01<P1: Pattern0<R1>, P2: Pattern1<P21, R21, R2>, R1, P21: Pattern<R21>, R21, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (R1, Components1<R21>) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
      f(r1, Components1(r21))
    } ?: notRightCls(r)

  fun thenIf(f: (R1, Components1<R21>) -> Boolean) = Then01(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (R1, Components1<R21>) -> Boolean) = Then01(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (R1, Components1<R21>) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (R1, Components1<R21>) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern0<R1>, P2: Pattern2<P21, P22, R21, R22, R2>, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then02(pat, {true})

class Then02<P1: Pattern0<R1>, P2: Pattern2<P21, P22, R21, R22, R2>, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (R1, Components2<R21, R22>) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
      f(r1, Components2(r21, r22))
    } ?: notRightCls(r)

  fun thenIf(f: (R1, Components2<R21, R22>) -> Boolean) = Then02(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (R1, Components2<R21, R22>) -> Boolean) = Then02(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (R1, Components2<R21, R22>) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (R1, Components2<R21, R22>) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern1<P11, R11, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, R11, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then10(pat, {true})

class Then10<P1: Pattern1<P11, R11, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, R11, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (Components1<R11>, R2) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
      f(Components1(r11), r2)
    } ?: notRightCls(r)

  fun thenIf(f: (Components1<R11>, R2) -> Boolean) = Then10(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (Components1<R11>, R2) -> Boolean) = Then10(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (Components1<R11>, R2) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (Components1<R11>, R2) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then20(pat, {true})

class Then20<P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (Components2<R11, R12>, R2) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r21, r22) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
      f(Components2(r21, r22), r2)
    } ?: notRightCls(r)

  fun thenIf(f: (Components2<R11, R12>, R2) -> Boolean) = Then20(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (Components2<R11, R12>, R2) -> Boolean) = Then20(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (Components2<R11, R12>, R2) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (Components2<R11, R12>, R2) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern1<P11, R11, R1>, P2: Pattern1<P21, R21, R2>, P11: Pattern<R11>, P21: Pattern<R21>, R11, R21, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then11(pat, {true})

class Then11<P1: Pattern1<P11, R11, R1>, P2: Pattern1<P21, R21, R2>, P11: Pattern<R11>, P21: Pattern<R21>, R11, R21, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (Components1<R11>, Components1<R21>) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
      val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
      f(Components1(r11), Components1(r21))
    } ?: notRightCls(r)

  fun thenIf(f: (Components1<R11>, Components1<R21>) -> Boolean) = Then11(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (Components1<R11>, Components1<R21>) -> Boolean) = Then11(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (Components1<R11>, Components1<R21>) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (Components1<R11>, Components1<R21>) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern1<P11, R11, R1>, P2: Pattern2<P21, P22, R21, R22, R2>, P11: Pattern<R11>, P21: Pattern<R21>, P22: Pattern<R22>, R11, R21, R22, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then12(pat, {true})

class Then12<P1: Pattern1<P11, R11, R1>, P2: Pattern2<P21, P22, R21, R22, R2>, P11: Pattern<R11>, P21: Pattern<R21>, P22: Pattern<R22>, R11, R21, R22, R1, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (Components1<R11>, Components2<R21, R22>) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
      val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
      f(Components1(r11), Components2(r21, r22))
    } ?: notRightCls(r)

  fun thenIf(f: (Components1<R11>, Components2<R21, R22>) -> Boolean) = Then12(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (Components1<R11>, Components2<R21, R22>) -> Boolean) = Then12(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (Components1<R11>, Components2<R21, R22>) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (Components1<R11>, Components2<R21, R22>) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}


fun <P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern1<P21, R21, R2>, P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, P21: Pattern<R21>, R21, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then21(pat, {true})

class Then21<P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern1<P21, R21, R2>, P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, P21: Pattern<R21>, R21, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (Components2<R11, R12>, Components1<R21>) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
      val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
      f(Components2(r11, r12), Components1(r21))
    } ?: notRightCls(r)

  fun thenIf(f: (Components2<R11, R12>, Components1<R21>) -> Boolean) = Then21(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (Components2<R11, R12>, Components1<R21>) -> Boolean) = Then21(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (Components2<R11, R12>, Components1<R21>) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (Components2<R11, R12>, Components1<R21>) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}

fun <P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern2<P21, P22, R21, R22, R2>, P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then22(pat, {true})

class Then22<P1: Pattern2<P11, P12, R11, R12, R1>, P2: Pattern2<P21, P22, R21, R22, R2>, P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R>(
  override val pat: Pattern2<P1, P2, R1, R2, R>,
  override val check: (R) -> Boolean
):  Stage<Pattern2<P1, P2, R1, R2, R>, R> {
  private fun <O> useComponents(r: R, f: (Components2<R11, R12>, Components2<R21, R22>) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      val (r11, r12) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
      val (r21, r22) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
      f(Components2(r11, r12), Components2(r21, r22))
    } ?: notRightCls(r)

  fun thenIf(f: (Components2<R11, R12>, Components2<R21, R22>) -> Boolean) = Then22(pat) { r: R -> useComponents(r, f) }
  fun thenIfThis(f: R.() -> (Components2<R11, R12>, Components2<R21, R22>) -> Boolean) = Then22(pat) { r: R -> useComponents(r, f(r)) }
  fun <O> then(f: (Components2<R11, R12>, Components2<R21, R22>) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
  fun <O> thenThis(f: R.() -> (Components2<R11, R12>, Components2<R21, R22>) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
}



//
//class Then111<P1: Pattern1<P11, R11, R1>, P2: Pattern1<P21, R21, R2>, P3: Pattern1<P31, R31, R3>,
//  P11: Pattern<R11>, P21: Pattern<R21>, P31: Pattern<R31>,
//  R11, R21, R31, R1, R2, R3, R>(
//  override val pat: Pattern3<P1, P2, P3, R1, R2, R3, R>,
//  override val check: (R) -> Boolean
//): Stage<Pattern3<P1, P2, P3, R1, R2, R3, R>, R> {
//  private fun <O> useComponents(r: R, f: (Components1<R11>, Components1<R21>, Components1<R31>) -> O): O =
//    (r as? ProductClass<*>)?.let {
//      val (r1, r2, r3) = pat.divideIntoComponentsAny(it)
//      val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
//      val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
//      val (r31) = pat.pattern3.divideIntoComponentsAny(r3 as Any)
//      f(Components1(r11), Components1(r21), Components1(r31))
//    } ?: notRightCls(r)
//
//  fun thenIf(f: (Components1<R11>, Components1<R21>, Components1<R31>) -> Boolean) =
//    Then111(pat) { r: R -> useComponents(r, f) }
//
//  fun thenIfThis(f: R.() -> (Components1<R11>, Components1<R21>, Components1<R31>) -> Boolean) =
//    Then111(pat) { r: R -> useComponents(r, f(r)) }
//
//  fun <O> then(f: (Components1<R11>, Components1<R21>, Components1<R31>) -> O): Case<O, R> =
//    StageCase(pat, check) { value -> useComponents(value, f) }
//
//  fun <O> thenThis(f: R.() -> (Components1<R11>, Components1<R21>, Components1<R31>) -> O): Case<O, R> =
//    StageCase(pat, check) { v -> useComponents(v, f(v)) }
//}
//
//class Then112<P1: Pattern1<P11, R11, R1>, P2: Pattern1<P21, R21, R2>, P3: Pattern3<P31, P32, P33, R31, R32, R33, R3>, P11: Pattern<R11>, P21: Pattern<R21>, P31: Pattern<R31>, P32: Pattern<R32>, P33: Pattern<R33>, R11, R21, R31, R32, R33, R1, R2, R3, R>(
//  override val pat: Pattern3<P1, P2, P3, R1, R2, R3, R>,
//  override val check: (R) -> Boolean
//): Stage<Pattern3<P1, P2, P3, R1, R2, R3, R>, R> {
//
//  private fun <O> useComponents(r: R, f: (R11, R21, R31, R32, R33) -> O): O =
//    (r as? ProductClass<*>)?.let {
//      val (r1, r2, r3) = pat.divideIntoComponentsAny(it)
//      val (r11) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
//      val (r21) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
//      val (r31, r32, r33) = pat.pattern3.divideIntoComponentsAny(r3 as Any)
//      f(r11, r21, r31, r32, r33)
//    } ?: notRightCls(r)
//
//  fun thenIf(f: (R11, R21, R31, R32, R33) -> Boolean) = Then112(pat) { r: R -> useComponents(r, f) }
//  fun thenIfThis(f: R.() -> (R11, R21, R31, R32, R33) -> Boolean) = Then112(pat) { r: R -> useComponents(r, f(r)) }
//  fun <O> then(f: (R11, R21, R31, R32, R33) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }
//  fun <O> thenThis(f: R.() -> (R11, R21, R31, R32, R33) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
//}
