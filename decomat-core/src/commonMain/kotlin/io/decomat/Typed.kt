package io.decomat

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

fun isType(value: kotlin.Any?, type: KType) =
  when (val cls = type.classifier) {
    is KClass<*> -> cls.isInstance(value)
    else -> false
  }

fun <T> T.isTypeOf(type: Typed<T>) =
  when (val cls = type.type.classifier) {
    is KClass<*> -> cls.isInstance(this)
    else -> false
  }
