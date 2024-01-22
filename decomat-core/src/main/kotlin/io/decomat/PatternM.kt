package io.decomat

import io.decomat.fail.fail
import io.decomat.fail.failToDivide

// TODO Describe how Comp2 turns into Comp0 etc... for the FlatMap case
abstract class Pattern2M<P1: Pattern<R1>, M, P2: Pattern<R2>, R1, R2, R>(val pattern1: P1, val pattern2: P2, override val typeR: Typed<R>):
    Pattern<R> {
  open override fun matches(instance: ProductClass<R>) =
    when(val inst = instance.isIfHas()) {
      is ProductClass2M<*, *, *, *> ->
        wrapNonComps<R1>(inst.a).let { pattern1.matches(it) }
          &&
          wrapNonComps<R2>(inst.b).let { pattern2.matches(it) }
      else -> false
    }

  // assumign the matches function already said 'false' if it doesn't match so at this point just throw an error
  open fun divideIntoComponentsAny(instance: kotlin.Any): Components2M<R1, M, R2> =
    when(instance) {
      is HasProductClass<*> ->
        divideIntoComponentsAny(instance.productComponents)
      is ProductClass2M<*, *, *, *> ->
        if (!isType(instance.value, typeR.type)) fail("Invalid type of data")  // todo refine message
        else divideIntoComponents(instance as ProductClass<R>)
      else -> fail("Cannot divide $instance into components. It is not a Product2 class.")
    }

  open fun divideIntoComponents(instance: ProductClass<R>): Components2M<R1, M, R2> =
    when(val inst = instance.isIfHas()) {
      is ProductClass2M<*, *, *, *> ->
        // for FlatMap_M: ~Pattern2<Query, Query> R1 and R2 will be Query
        // (i wrote ~Pattern2<...> because it's HasProductComponents)
        Components2M(inst.a as R1, inst.m as M, inst.b as R2)
      else -> fail("must match properly") // todo refine message
    }
}
