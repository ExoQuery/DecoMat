package io.decomat

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class Is<R> private constructor (private val type: Typed<R>, private val valueCompare: ValueCompare<R>): Pattern0<R>(type) {
  override fun matches(r: ProductClass<R>): Boolean =
    type.typecheck(r.value) && when(valueCompare) {
      is DoCompare -> r.value == valueCompare.value
      is DoComparePredicate -> valueCompare.f(r.value)
      is DontCompare -> true
    }

  companion object {
    private sealed interface ValueCompare<out R>
    private data class DoCompare<R>(val value: R): ValueCompare<R>
    private data class DoComparePredicate<R>(val f: (R) -> Boolean): ValueCompare<R>
    private object DontCompare: ValueCompare<Nothing>

    fun <R> TypedAs(type: Typed<R>) = Is(type, DontCompare)
    fun <R> ValuedAs(type: Typed<R>, value: R) = Is(type, DoCompare(value))
    fun <R> PredicateAs(type: Typed<R>, value: (R) -> Boolean) = Is(type, DoComparePredicate(value))
    inline operator fun <reified R> invoke(): Is<R> = Is.TypedAs(Typed<R>())
    inline operator fun <reified R> invoke(value: R): Is<R> = Is.ValuedAs(Typed<R>(), value)
    inline operator fun <reified R> invoke(noinline f: (R) -> Boolean): Is<R> = Is.PredicateAs(Typed<R>(), f)
  }
}

inline fun <reified R> IsAny() = Is<R>()
