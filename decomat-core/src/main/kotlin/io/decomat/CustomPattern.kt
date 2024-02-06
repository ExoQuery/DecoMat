package io.decomat

import io.decomat.fail.*

inline fun <P1 : Pattern<R1>, R1, reified R> customPattern1(
  nested1: P1,
  noinline matchName: (R) -> Components1<R1>?
): Pattern1<P1, R1, R> = CustomPattern1(nested1, matchName, Typed<R>())

inline fun <P1 : Pattern<R1>, P2 : Pattern<R2>, R1, R2, reified R> customPattern2(
  nested1: P1,
  nested2: P2,
  noinline matchName: (R) -> Components2<R1, R2>?
): Pattern2<P1, P2, R1, R2, R> = CustomPattern2(nested1, nested2, matchName, Typed<R>())

class CustomPattern1<P1 : Pattern<R1>, R1, R>(
  val innerMatch: P1,
  val match: (R) -> Components1<R1>?,
  val tpe: Typed<R>
): Pattern1<P1, R1, R>(innerMatch, tpe) {
  override fun matches(comps: ProductClass<R>): Boolean =
    match(comps.value).let {
      it != null && innerMatch.matchesAny(it.a as Any)
    }

  override fun divideIntoComponents(instance: ProductClass<R>): Components1<R1> =
    match(instance.value) ?: failToDivide(instance)

  override fun divideIntoComponentsAny(instance: kotlin.Any): Components1<R1> =
    if (isType(instance, tpe.type))
      match(instance as R) ?: failToDivide(instance)
    else
      failToDivide(instance)
}



class CustomPattern2<P1 : Pattern<R1>, P2: Pattern<R2>, R1, R2, R>(
  val innerMatchA: P1,
  val innerMatchB: P2,
  val match: (R) -> Components2<R1, R2>?,
  val tpe: Typed<R>
): Pattern2<P1, P2, R1, R2, R>(innerMatchA, innerMatchB, tpe) {
  override fun matches(comps: ProductClass<R>): Boolean =
    match(comps.value).let {
      it != null &&
        innerMatchA.matchesAny(it.a as Any) &&
        innerMatchB.matchesAny(it.b as Any)
    }

  override fun divideIntoComponents(instance: ProductClass<R>): Components2<R1, R2> =
    match(instance.value) ?: failToDivide(instance)

  override fun divideIntoComponentsAny(instance: kotlin.Any): Components2<R1, R2> =
    if (isType(instance, tpe.type))
      match(instance as R) ?: failToDivide(instance)
    else
      failToDivide(instance)
}



class CustomPattern2M<P1 : Pattern<R1>, M, P2: Pattern<R2>, R1, R2, R>(
  val innerMatchA: P1,
  val innerMatchB: P2,
  val match: (R) -> Components2M<R1, M, R2>?,
  val tpe: Typed<R>
): Pattern2M<P1, M, P2, R1, R2, R>(innerMatchA, innerMatchB, tpe) {
  override fun matches(comps: ProductClass<R>): Boolean =
    match(comps.value).let {
      it != null &&
        innerMatchA.matchesAny(it.a as Any) &&
        innerMatchB.matchesAny(it.b as Any)
    }

  override fun divideInto3Components(instance: ProductClass<R>): Components2M<R1, M, R2> =
    match(instance.value) ?: failToDivide(instance)

  override fun divideInto3ComponentsAny(instance: kotlin.Any): Components2M<R1, M, R2> =
    if (isType(instance, tpe.type))
      match(instance as R) ?: failToDivide(instance)
    else
      failToDivide(instance)
}


