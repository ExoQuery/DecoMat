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
          wrapNonComps<R2>(inst.getMProdB()).let { pattern2.matches(it) }
      else -> false
    }

  // use 3-component division in then then-functions for Pattern2M-family but
  // use the 2-component division in the superclass of this in the Pattenr2M family
  // (the fact that we do this allows us to reuse then thenXX methods for the Pattern2M family
  // otherwise would would need to reimplement all of them for Pattern2M giving it the performance
  // probem of Pattern3)
  open fun divideInto3ComponentsAny(instance: kotlin.Any): Components2M<R1, M, R2> =
    when(instance) {
      is HasProductClass<*> ->
        divideInto3ComponentsAny(instance.productComponents)
      is ProductClass2M<*, *, *, *> ->
        if (!isType(instance.value, typeR.type)) fail("Invalid type of data")  // todo refine message
        else divideInto3Components(instance as ProductClass<R>)
      else -> fail("Cannot divide $instance into components. It is not a Product2 class.")
    }

  open fun divideInto3Components(instance: ProductClass<R>): Components2M<R1, M, R2> =
    when(val inst = instance.isIfHas()) {
      is ProductClass2M<*, *, *, *> ->
        // for FlatMap_M: ~Pattern2<Query, Query> R1 and R2 will be Query
        // (i wrote ~Pattern2<...> because it's HasProductComponents)
        Components2M(inst.a as R1, inst.m as M, inst.getMProdB() as R2)
      else -> fail("must match properly") // todo refine message
    }
}
