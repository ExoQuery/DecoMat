package io.decomat

class Is<R> private constructor (private val type: Typed<R>, private val valueCompare: ValueCompare<R>): Pattern0<R>(type) {
  override fun matches(r: ProductClass<R>): Boolean =
    isType(r.value, type.type) &&
      when(valueCompare) {
        is DoCompare -> r.value == valueCompare.value
        is DontCompare -> true
      }

  companion object {
    private sealed interface ValueCompare<out R>
    private data class DoCompare<R>(val value: R): ValueCompare<R>
    private object DontCompare: ValueCompare<Nothing>

    fun <R> TypedAs(type: Typed<R>) = Is(type, DontCompare)
    fun <R> ValuedAs(type: Typed<R>, value: R) = Is(type, DoCompare(value))
    inline operator fun <reified R> invoke(): Is<R> = Is.TypedAs(Typed<R>())
    inline operator fun <reified R> invoke(value: R): Is<R> = Is.ValuedAs(Typed<R>(), value)
  }
}

inline fun <reified R> IsAny() = Is<R>()
