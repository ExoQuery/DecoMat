package io.decomat

import io.decomat.fail.fail
import io.decomat.fail.failToDivide

/**
 * Note that all the patterns need to be covariant, otherwise something like this:
 * `Pattern1<Pattern1<Pattern<X>, R1>, R>` won't be a subtype of something like:
 * `Pattern1<Pattern1<Pattern0<String>, Name>, Person>` because Pattern0<String>
 *  won't be unified with `Pattern<X>`. Therefore all inner-patterns need
 *  to be covariant.
 */
sealed interface Pattern<out R> {
  val typeR: Typed<out R>
  fun matches(comps: ProductClass<@UnsafeVariance R>): Boolean
  @Suppress("UNCHECKED_CAST")
  fun matchesAny(comps: Any): Boolean =
    when(comps) {
      is ProductClass<*> -> {
        typeR.typecheck(comps.productClassValue) && matches(comps as ProductClass<R>)
      }
      else -> {
        val compsReal = ProductClass0(comps)
        typeR.typecheck(compsReal.productClassValue) && matches(compsReal as ProductClass<R>)
      }
    }
}



// TODO write a custom function with Pattern etc... that is equivalen of `unapply`. They need to be open classes
abstract class Pattern0<out R>(override val typeR: Typed<@UnsafeVariance R>): Pattern<R> {
  // `matches` function is delegated to implementors e.g. `Any`
}

abstract class Pattern1<out P1: Pattern<R1>, out R1, out R>(val pattern1: P1, override val typeR: Typed<@UnsafeVariance R>): Pattern<R> {
  override fun matches(comps: ProductClass<@UnsafeVariance R>) =
  // E.g. for Distinct.M(...): Pattern1<...> check that the thing we are trying to match as `Distinct`
    // is actually a `Distinct` instances
    typeR.typecheck(comps.productClassValue)
      &&
      when(val compsDef = comps.isIfHas()) {
        is ProductClass1<*, *> ->
          wrapNonComps<R1>(compsDef.a).let { pattern1.matches(it) }
        else -> false
      }

  open fun divideIntoComponentsAny(instance: kotlin.Any): Components1<@UnsafeVariance R1> =
    when(instance) {
      is HasProductClass<*> ->
        divideIntoComponentsAny(instance.productComponents)
      is ProductClass1<*, *> ->
        if (!typeR.typecheck(instance.productClassValue)) fail("The type ${instance.productClassValue} has an unexpected return type")
        else divideIntoComponents(instance as ProductClass<R>)
      else -> failToDivide(instance)
    }

  open fun divideIntoComponents(instance: ProductClass<@UnsafeVariance R>): Components1<@UnsafeVariance R1> =
    when(val inst = instance.isIfHas()) {
      is ProductClass1<*, *> -> Components1(inst.a as R1)
      else -> fail("must match properly") // todo refine message
    }
}

// TODO Describe how Comp2 turns into Comp0 etc... for the FlatMap case
abstract class Pattern2<out P1: Pattern<R1>, out P2: Pattern<R2>, out R1, out R2, out R>(val pattern1: P1, val pattern2: P2, override val typeR: Typed<@UnsafeVariance R>):
    Pattern<R> {
  open override fun matches(instance: ProductClass<@UnsafeVariance R>) =
    when(val inst = instance.isIfHas()) {
      is ProductClass2<*, *, *> ->
        wrapNonComps<R1>(inst.a).let { pattern1.matches(it) }
          &&
          wrapNonComps<R2>(inst.b).let { pattern2.matches(it) }
      else -> false
    }

  // assumign the matches function already said 'false' if it doesn't match so at this point just throw an error
  open fun divideIntoComponentsAny(instance: kotlin.Any): Components2<@UnsafeVariance R1, @UnsafeVariance R2> =
    when(instance) {
      is HasProductClass<*> ->
        divideIntoComponentsAny(instance.productComponents)
      is ProductClass2<*, *, *> ->
        if (!typeR.typecheck(instance.productClassValue)) fail("Invalid type of data")  // todo refine message
        else divideIntoComponents(instance as ProductClass<R>)
      else -> fail("Cannot divide $instance into components. It is not a Product2 class.")
    }

  open fun divideIntoComponents(instance: ProductClass<@UnsafeVariance R>): Components2<@UnsafeVariance R1, @UnsafeVariance R2> =
    when(val inst = instance.isIfHas()) {
      is ProductClass2<*, *, *> ->
        // for FlatMap_M: ~Pattern2<Query, Query> R1 and R2 will be Query
        // (i wrote ~Pattern2<...> because it's HasProductComponents)
          Components2(inst.a as R1, inst.b as R2)
      else -> fail("must match properly") // todo refine message
    }
}
