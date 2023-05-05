package io.decomat

import io.decomat.Typed

private fun fail(msg: String): Nothing = throw IllegalArgumentException(msg)

// TODO need to test this with null
@Suppress("UNCHECKED_CAST")
internal fun <C> wrapNonComps(a: kotlin.Any?) =
  when(a) {
    is ProductClass<*> ->
      a as ProductClass<C>  // nested components
    else ->
      ProductClass0(a) as ProductClass<C> // leaf level entity
  }



//fun on(value: kotlin.Any): DoMatch = DoMatch(value)

// TODO Have an else-clause
//class DoMatch(val value: kotlin.Any) {
//  fun <O> match(vararg cases: Case<O>): O? =
//    cases.find { theCase -> theCase.matches(value) }?.eval(value)
//}


data class Stage<P, R>(val pat: P, val check: (R) -> Boolean)


interface Case<O> {
  fun matches(value: kotlin.Any): Boolean
  fun eval(value: kotlin.Any): O
  fun evalSafe(value: kotlin.Any): O? =
    if (!matches(value)) null
    else eval(value)
}
