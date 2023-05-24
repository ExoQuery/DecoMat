package io.decomat

sealed interface Pattern<R> {
  val typeR: Typed<R>
  fun matches(comps: ProductClass<R>): Boolean
  @Suppress("UNCHECKED_CAST")
  fun matchesAny(comps: ProductClass<*>): Boolean =
    isType(comps.value, typeR.type) && matches(comps as ProductClass<R>)
}

// TODO write a custom function with Pattern etc... that is equivalen of `unapply`. They need to be open classes
abstract class Pattern0<R>(override val typeR: Typed<R>): Pattern<R> {
  // `matches` function is delegated to implementors e.g. `Any`
}

abstract class Pattern1<P1: Pattern<R1>, R1, R>(val pattern1: P1, override val typeR: Typed<R>): Pattern<R> {
  override fun matches(comps: ProductClass<R>) =
  // E.g. for Distinct.M(...): Pattern1<...> check that the thing we are trying to match as `Distinct`
    // is actually a `Distinct` instances
    comps.value.isType(typeR)
      &&
      when(val compsDef = comps.isIfHas()) {
        is ProductClass1<*, *> ->
          wrapNonComps<R1>(compsDef.a).let { pattern1.matches(it) }
        else -> false
      }

  open fun divideIntoComponentsAny(instance: kotlin.Any): Components1<R1> =
    when(instance) {
      is HasProductClass<*> ->
        divideIntoComponentsAny(instance.productComponents)
      is ProductClass1<*, *> ->
        if (!isType(
                instance.value,
                typeR.type
            )
        ) fail("The type ${instance.value} is not a ${typeR.type}")  // todo refine message
        else divideIntoComponents(instance as ProductClass<R>)
      else -> failToDivide(instance)
    }

  protected fun failToDivide(value: kotlin.Any): Nothing =
    fail("Cannot divide $value into components. It is not a product-class.")

  open fun divideIntoComponents(instance: ProductClass<R>): Components1<R1> =
    when(val inst = instance.isIfHas()) {
      is ProductClass1<*, *> -> Components1(inst.a as R1)
      else -> fail("must match properly") // todo refine message
    }
}

// TODO Describe how Comp2 turns into Comp0 etc... for the FlatMap case
abstract class Pattern2<P1: Pattern<R1>, P2: Pattern<R2>, R1, R2, R>(val pattern1: P1, val pattern2: P2, override val typeR: Typed<R>):
    Pattern<R> {
  override fun matches(instance: ProductClass<R>) =
    when(val inst = instance.isIfHas()) {
      is ProductClass2<*, *, *> ->
        wrapNonComps<R1>(inst.a).let { pattern1.matches(it) }
          &&
          wrapNonComps<R2>(inst.b).let { pattern2.matches(it) }
      else -> false
    }

  // assumign the matches function already said 'false' if it doesn't match so at this point just throw an error
  fun divideIntoComponentsAny(instance: kotlin.Any): Components2<R1, R2> =
    when(instance) {
      is HasProductClass<*> ->
        divideIntoComponentsAny(instance.productComponents)
      is ProductClass2<*, *, *> ->
        if (!isType(instance.value, typeR.type)) fail("Invalid type of data")  // todo refine message
        else divideIntoComponents(instance as ProductClass<R>)
      else -> fail("Cannot divide $instance into components. It is not a Product2 class.")
    }

  fun divideIntoComponents(instance: ProductClass<R>): Components2<R1, R2> =
    when(val inst = instance.isIfHas()) {
      is ProductClass2<*, *, *> ->
        // for FlatMap_M: ~Pattern2<Query, Query> R1 and R2 will be Query
        // (i wrote ~Pattern2<...> because it's HasProductComponents)
          Components2(inst.a as R1, inst.b as R2)
      else -> fail("must match properly") // todo refine message
    }
}

abstract class Pattern3<P1: Pattern<R1>, P2: Pattern<R2>, P3: Pattern<R3>, R1, R2, R3, R>(val pattern1: P1, val pattern2: P2, val pattern3: P3, override val typeR: Typed<R>):
    Pattern<R> {
  override fun matches(comps: ProductClass<R>) =
    comps.value.isType(typeR)
      &&
      when(val compsDef = comps.isIfHas()) {
        is ProductClass3<*, *, *, *> ->
          wrapNonComps<R1>(compsDef.a).let { pattern1.matches(it) }
            &&
            wrapNonComps<R2>(compsDef.b).let { pattern2.matches(it) }
            &&
            wrapNonComps<R3>(compsDef.c).let { pattern3.matches(it) }
        else -> false
      }

  fun divideIntoComponentsAny(instance: kotlin.Any): Components3<R1, R2, R3> =
    when(instance) {
      is HasProductClass<*> ->
        divideIntoComponentsAny(instance.productComponents)
      is ProductClass3<*, *, *, *> ->
        if (!isType(instance.value, typeR.type)) fail("Invalid type of data")  // todo refine message
        else divideIntoComponents(instance as ProductClass<R>)
      else -> fail("Cannot divide $instance into components. It is not a Product2 class.")
    }

  fun divideIntoComponents(instance: ProductClass<R>): Components3<R1, R2, R3> =
    when(val inst = instance.isIfHas()) {
      is ProductClass3<*, *, *, *> ->
        Components3(inst.a as R1, inst.b as R2, inst.c as R3)
      else -> fail("must match properly") // todo refine message
    }
}

private fun fail(msg: String): Nothing = throw IllegalArgumentException(msg)