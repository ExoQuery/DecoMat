package io.decomat

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

// Holder that we can use in various paces that guarentees the KType is the same as a parameter type
// Originally there was a KClass<T> here for diagnostic information but I have removed that because
// the reflection is expensive. If I want to add it back just make sure it is lazy
// e.g. the invoke() passes a `{ T:class }` into the constructor which takes a () -> KClass
// element. Then return the type name via a typeName method or getter property.
// Also, perhaps this should be controlled by a System property
// e.g. we can check if System.property("decomat.diagnostic.info") is true and the
// only propagate the reflection information if it is.
class Typed<T>(val typecheck: (Any?) -> Boolean) {
  companion object {
    inline operator fun <reified T> invoke() = Typed<T>({ it is T })
  }
}

internal class IsAnyBase: Pattern0<Nothing>(NothingWrapper.Nothing) {
  override fun matches(comps: ProductClass<Nothing>): Boolean  = true
  object NothingWrapper {
    private val nothing: Nothing get() = TODO("Nothing can't be ever assigned or returned")
    val Nothing = Typed<Nothing>({ false })
  }
}

private object NothingWrapper {
  private val nothing: Nothing get() = TODO("Nothing can't be ever assigned or returned")
  val Nothing = Typed<Nothing>({ false })
}
