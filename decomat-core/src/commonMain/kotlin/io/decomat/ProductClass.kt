package io.decomat

import kotlin.reflect.KProperty1

sealed interface ProductClass<out T> {
  val value: T
  fun isIfHas() =
    when(val thisComp = this) {
      is HasProductClass<T> -> thisComp.productComponents
      else -> this
    }
}

// Does not seem to work, no annotations are found
//@Suppress("UNCHECKED_CAST")
//interface HasProductClassAuto<T>: HasProductClass<T> {
//  override val productComponents: ProductClass<T> get() =
//    cache.computeIfAbsent(this) { _doInit() } as ProductClass<T>
//
//  fun _doInit(): ProductClass<T> {
//    val cls = this::class
//    val ctor = cls.primaryConstructor ?: fail("No primary constructor found in the class ${this}")
//    println(cls.memberProperties.map { "${it.name} (${it.annotations})" })
//    val componentNames = ctor.parameters.filter { it.annotations.any { anno -> anno.annotationClass.qualifiedName == "io.decomat.Component" } }.map { it.name }
//    if (componentNames.isEmpty()) fail("No components annotated with @Component found in the class ${this}. Found components: ${ctor.parameters.map { it.name }}.")
//    val components: List<KProperty1<out HasProductClassAuto<T>, *>> =
//      cls.memberProperties.filter { componentNames.contains(it.name) }
//    if (components.size != componentNames.size)
//      fail("Not all the parameters with @Component annoations (${componentNames.joinToString(", ")}) were found to be components (${components.joinToString { "," }})")
//
//    fun comp(i: Int) = components[i].getter.call(this)
//
//    val productClass =
//      when(components.size) {
//        1 -> ProductClass1(this as T, comp(0))
//        2 -> ProductClass2(this as T, comp(0), comp(1))
//        3 -> ProductClass3(this as T, comp(0), comp(1), comp(2))
//        else -> fail("Num components needs to be 1, 2, or 3 but was: ${components.size}")
//      }
//
//    return productClass
//  }
//
//  // Sigh, need to use a global cache to compute product-classes we've since since you can't assign
//  // values directly to varaibles inside of kotlin interfaces
//  companion object {
//    val cache = WeakHashMap<HasProductClass<*>, ProductClass<*>>()
//  }
//}

interface HasProductClass<T>: ProductClass<T> {
  val productComponents: ProductClass<T>
  override val value get() = productComponents.value
}

fun <T> productComponentsOf(host: T) = ProductClass0(host)
fun <T, A> productComponentsOf(host: T, componentA: A) = ProductClass1(host, componentA)
fun <T, A, B> productComponentsOf(host: T, componentA: A, componentB: B) = ProductClass2(host, componentA, componentB)
fun <T, A, M, B> productComponentsOf(host: T, componentA: A, componentM: M, componentB: B) = ProductClass2M(host, componentA, componentM, componentB)

data class ProductClass0<T>(override val value: T): ProductClass<T>
data class ProductClass1<T, A>(override val value: T, val a: A): ProductClass<T>
data class ProductClass2<T, A, B>(override val value: T, val a: A, val b: B): ProductClass<T> {
  val matchComp get(): Components2<A, B> = Components2(a, b)
}

data class ProductClass2M<T, A, M, B>(override val value: T, val a: A, val m: M, val b: B): ProductClass<T> {
  val matchComp get(): Components2M<A, M, B> = Components2M(a, m, b)
}

/** I think in order to avoid nastiness in kapshot experiments with hard-typing this has to be
 * covariant, need to test!
 */


sealed interface Components
data class Components1<in A>(val a: @UnsafeVariance A): Components
// For example: data class FlatMap(head: Query, body: Query) extends Comp2<Query, Query>
data class Components2<in A, in B>(val a: @UnsafeVariance A, val b: @UnsafeVariance B): Components

data class Components2M<in A, in M, in B>(val a: @UnsafeVariance A, val m: @UnsafeVariance M, val b: @UnsafeVariance B): Components
