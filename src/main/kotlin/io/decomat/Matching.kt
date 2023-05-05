package io.decomat

import io.decomat.Typed

private fun fail(msg: String): Nothing = throw IllegalArgumentException(msg)

// TODO need to test this with null
@Suppress("UNCHECKED_CAST")
internal fun <C> wrapNonComps(a: Any?) =
  when(a) {
    is ProductClass<*> ->
      a as ProductClass<C>  // nested components
    else ->
      ProductClass0(a) as ProductClass<C> // leaf level entity
  }



fun on(value: Any): DoMatch = DoMatch(value)

// TODO Have an else-clause
class DoMatch(val value: Any) {
  fun <O> match(vararg cases: Case<O>): O? =
    cases.find { theCase -> theCase.matches(value) }?.eval(value)
}

interface Case<O> {
  fun matches(value: Any): Boolean
  fun eval(value: Any): O
  fun evalSafe(value: Any): O? =
    if (!matches(value)) null
    else eval(value)
}
