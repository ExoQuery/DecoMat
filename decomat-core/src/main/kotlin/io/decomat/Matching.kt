package io.decomat

// TODO need to test this with null
@Suppress("UNCHECKED_CAST")
internal fun <C> wrapNonComps(a: kotlin.Any?) =
  when(a) {
    is ProductClass<*> ->
      a as ProductClass<C>  // nested components
    else ->
      ProductClass0(a) as ProductClass<C> // leaf level entity
  }



fun <M> on(value: M): DoMatch<M> = DoMatch(value)

// TODO Have an else-clause
class DoMatch<R>(val value: R) {
  fun <O> match(vararg cases: Case<O, R>): O? =
    cases.find { theCase -> theCase.matches(value) }?.eval(value)

//  fun <O> matchAny(vararg cases: Case<O, R>): O? =
//    cases.find { theCase -> theCase.matches(value as Any) }?.eval(value as Any)
}


interface Stage<P, R> {
  val pat: P
  val check: (R) -> Boolean
  fun notRightCls(value: R): Nothing = throw IllegalArgumentException("The value $value was not a ProductClass")
}


interface Case<O, in R> {
  fun matches(value: R): Boolean
  fun eval(value: R): O
  fun evalSafe(value: R): O? =
    if (!matches(value)) null
    else eval(value)
}

inline fun <O, reified R> Case<O, R>.matches(value: Any) =
  this.matches(value as? R ?: throw IllegalArgumentException("The value $value is not of type ${R::class.qualifiedName}"))

inline fun <O, reified R> Case<O, R>.eval(value: Any) =
  this.eval(value as? R ?: throw IllegalArgumentException("The value $value is not of type ${R::class.qualifiedName}"))


class StageCase<O, R>(
  val pat: Pattern<R>,
  val check: (R) -> Boolean,
  val evalCase: (R) -> O
): Case<O, R> {
  override fun matches(value: R): Boolean =
    (value as? ProductClass<*>)?.let { pat.matchesAny(it) && check(value) } ?: false
  override fun eval(value: R): O = evalCase(value)
}