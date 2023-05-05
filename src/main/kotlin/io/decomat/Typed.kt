package io.decomat

import io.decomat.Pattern0
import io.decomat.ProductClass
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

// Holder that we can use in various paces that guarentees the KType is the same as a parameter type
class Typed<T> private constructor (val type: KType) {
  companion object {
    fun <T> withType(type: KType) = Typed<T>(type)
    inline operator fun <reified T> invoke() = withType<T>(typeOf<T>())
  }
}

@JvmName("isTypeFun")
fun isType(value: kotlin.Any?, type: KType) =
  when (val cls = type.classifier) {
    is KClass<*> -> cls.isInstance(value)
    else -> false
  }

@JvmName("isTypeOp2")
fun <T> T.isType(type: KType) =
  when (val cls = type.classifier) {
    is KClass<*> -> cls.isInstance(this)
    else -> false
  }

@JvmName("isTypeOp1")
fun <T> T.isType(type: Typed<T>) =
  when (val cls = type.type.classifier) {
    is KClass<*> -> cls.isInstance(this)
    else -> false
  }

internal class IsAnyBase: Pattern0<Nothing>(NothingWrapper.Nothing) {
  override fun matches(comps: ProductClass<Nothing>): Boolean  = true
  object NothingWrapper {
    private val nothing: Nothing get() = TODO("Nothing can't be ever assigned or returned")
    val Nothing = Typed.withType<Nothing>(this::nothing.returnType)
  }
}

private object NothingWrapper {
  private val nothing: Nothing get() = TODO("Nothing can't be ever assigned or returned")
  val Nothing = Typed.withType<Nothing>(this::nothing.returnType)
}
