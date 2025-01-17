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



fun <M, O> M.match(vararg cases: Case<O, Any>): O? =
  cases.find { theCase -> theCase.matches(this as Any) }?.eval(this as Any)

fun <M> on(value: M): DoMatch<M> = DoMatch(value)

// TODO Have an else-clause
class DoMatch<in R>(private val value: R) {
  /** Don't want to be strict about what types the case-has (i.e. R) since
   * if they don't match the 1st part `theCase.matches` will just return `false` and
   * evaluation will not proceed. Also, this is needed when we want to do
   * contravariant matches (i.e. the thing we're matching-on is more general
   * then the thing we are trying to match it to).
   */
  fun <O> match(vararg cases: Case<O, Any>): O? =
    cases.find { theCase -> theCase.matches(value as Any) }?.eval(value as Any)

//  fun <O> matchAny(vararg cases: Case<O, R>): O? =
//    cases.find { theCase -> theCase.matches(value as Any) }?.eval(value as Any)
}


interface Stage<P, R> {
  val pat: P
  val check: (R) -> Boolean
  fun notRightCls(value: R): Nothing = throw IllegalArgumentException("The value $value was not a ProductClass")
}


interface Case<O, out R> {
  fun matches(value: @UnsafeVariance R): Boolean
  fun eval(value: @UnsafeVariance R): O
  fun evalSafe(value: @UnsafeVariance R): O? =
    if (!matches(value)) null
    else eval(value)
}

inline fun <O, reified R> Case<O, R>.matches(value: Any) =
  this.matches(value as? R ?: throw IllegalArgumentException("The value $value is not of type ${R::class}"))

inline fun <O, reified R> Case<O, R>.eval(value: Any) =
  this.eval(value as? R ?: throw IllegalArgumentException("The value $value is not of type ${R::class}"))


class StageCase<O, out R> private constructor (
  private val pat: Pattern<R>,
  private val check: (R) -> Boolean,
  private val evalCase: (R) -> O
): Case<O, R> {
  // NOTE: The typing here is WRONG! `@UnsafeVariance R` has to be treated as `Any` which is indeed possible in this case.
  // that is why we need to do matchesAny FIRST because that actually does a typecheck and only then can we do check(value)
  override fun matches(value: @UnsafeVariance R): Boolean =
    pat.matchesAny(value as Any) && check(value)

  override fun eval(value: @UnsafeVariance R): O = evalCase(value)

  /**
   * The reason we introduce this companion constructor that generaalizes
   * StageCase<O, R> to Case<O, R> is because we
   * want to return Case<O, R> here instead of StageCase<O, R>
   * since it makes certain errors in pattern matching simpler to understand
   * e.g: something like this:
   * Required:
   *   Case<TypeVariable(O), Name>
   *   Found:
   *   StageCase<TypeVariable(O), FullName>
   * -- Misleads the user to to think that it's found StageCase instead of the required 'Case'
   * Instead the error now reads like this:
   * Required:
   *   Case<TypeVariable(O), Name>
   *   Found:
   *   Case<TypeVariable(O), FullName>
   *
   * It is noteworthy that this is a general pattern for product types in Scala (3), Haskell and
   * other languages. For example, in Scala 3, using a `enum case...` datatype will always
   * cause the constructor to instantiate the general type.
   */
  companion object {
    operator fun <O, R> invoke(
      pat: Pattern<R>,
      check: (R) -> Boolean,
      evalCase: (R) -> O
    ): Case<O, R> =
      StageCase(pat, check, evalCase)
  }
}