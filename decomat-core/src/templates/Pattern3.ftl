<#-- Define the template for generating Then classes -->
<#-- Start with the necessary imports -->
<#-- Define the ranges for the combinations -->
<#list 1..3 as P1>
    <#list 1..3 as P2>
        <#list 1..3 as P3>

          class Then${P1}${P2}${P3}<
            <#list 1..P1 as I1>
              P1<I1: Pattern${I1}<P1${I1}1, <#if I1 gt 1><#list 2..I1 as J1>R1${J1},</#list></#if> R1${I1}, R1>,
            </#list>
            <#list 1..P2 as I2>
              P2<I2: Pattern${I2}<P2${I2}1, <#if I2 gt 1><#list 2..I2 as J2>R2${J2},</#list></#if> R2${I2}, R2>,
            </#list>
            <#list 1..P3 as I3>
              P3<I3: Pattern${I3}<P3${I3}1, <#if I3 gt 1><#list 2..I3 as J3>R3${J3},</#list></#if> R3${I3}, R3>,
            </#list>
            <#list 1..P1 as I1>
              P1${I1}1: Pattern<R1${I1}>, <#if I1 gt 1><#list 2..I1 as J1>R1${J1},</#list></#if>
            </#list>
            <#list 1..P2 as I2>
              P2${I2}1: Pattern<R2${I2}>, <#if I2 gt 1><#list 2..I2 as J2>R2${J2},</#list></#if>
            </#list>
            <#list 1..P3 as I3>
              P3${I3}1: Pattern<R3${I3}>, <#if I3 gt 1><#list 2..I3 as J3>R3${J3},</#list></#if>
            </#list>
          R1, R2, R3, R>(
          override val pat: Pattern3<
            <#list 1..P1 as I1>P1<I1><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>P2<I2><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>P3<I3><#if I3 < P3>, </#if></#list>,
          R1, R2, R3, R>,
          override val check: (R) -> Boolean
          ): Stage<Pattern3<
            <#list 1..P1 as I1>P1<I1><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>P2<I2><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>P3<I3><#if I3 < P3>, </#if></#list>,
          R1, R2, R3, R>, R> {

          inline fun <O> useComponents(r: R, f: (
            <#list 1..P1 as I1>Components${I1}<#list 1..I1 as J1>R1${J1}<#if J1 < I1>,</#if></#list><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>Components${I2}<#list 1..I2 as J2>R2${J2}<#if J2 < I2>,</#if></#list><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>Components${I3}<#list 1..I3 as J3>R3${J3}<#if J3 < I3>,</#if></#list><#if I3 < P3>, </#if></#list>
          ) -> O): O =
          (r as? ProductClass<*>)?.let {
          val (r1, r2, r3) = pat.divideIntoComponentsAny(it)
            <#list 1..P1 as I1>
              val (<#list 1..I1 as J1>r1${J1}<#if J1 < I1>, </#if></#list>) = pat.pattern1.divideIntoComponentsAny(r1 as Any)
            </#list>
            <#list 1..P2 as I2>
              val (<#list 1..I2 as J2>r2${J2}<#if J2 < I2>, </#if></#list>) = pat.pattern2.divideIntoComponentsAny(r2 as Any)
            </#list>
            <#list 1..P3 as I3>
              val (<#list 1..I3 as J3>r3${J3}<#if J3 < I3>, </#if></#list>) = pat.pattern3.divideIntoComponentsAny(r3 as Any)
            </#list>
          f(
            <#list 1..P1 as I1>Components${I1}(<#list 1..I1 as J1>r1${J1}<#if J1 < I1>, </#if></#list>)<#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>Components${I2}(<#list 1..I2 as J2>r2${J2}<#if J2 < I2>, </#if></#list>)<#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>Components${I3}(<#list 1..I3 as J3>r3${J3}<#if J3 < I3>, </#if></#list>)<#if I3 < P3>, </#if></#list>
          )
          } ?: notRightCls(r)

          inline fun thenIf(crossinline f: (
            <#list 1..P1 as I1>Components${I1}<#list 1..I1 as J1>R1${J1}<#if J1 < I1>, </#if></#list><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>Components${I2}<#list 1..I2 as J2>R2${J2}<#if J2 < I2>, </#if></#list><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>Components${I3}<#list 1..I3 as J3>R3${J3}<#if J3 < I3>, </#if></#list><#if I3 < P3>, </#if></#list>
          ) -> Boolean) = Then${P1}${P2}${P3}(pat) { r: R -> useComponents(r, f) }

          inline fun thenIfThis(crossinline f: R.() -> (
            <#list 1..P1 as I1>Components${I1}<#list 1..I1 as J1>R1${J1}<#if J1 < I1>, </#if></#list><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>Components${I2}<#list 1..I2 as J2>R2${J2}<#if J2 < I2>, </#if></#list><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>Components${I3}<#list 1..I3 as J3>R3${J3}<#if J3 < I3>, </#if></#list><#if I3 < P3>, </#if></#list>
          ) -> Boolean) = Then${P1}${P2}${P3}(pat) { r: R -> useComponents(r, f(r)) }

          inline fun <O> then(crossinline f: (
            <#list 1..P1 as I1>Components${I1}<#list 1..I1 as J1>R1${J1}<#if J1 < I1>, </#if></#list><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>Components${I2}<#list 1..I2 as J2>R2${J2}<#if J2 < I2>, </#if></#list><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>Components${I3}<#list 1..I3 as J3>R3${J3}<#if J3 < I3>, </#if></#list><#if I3 < P3>, </#if></#list>
          ) -> O): Case<O, R> = StageCase(pat, check) { value -> useComponents(value, f) }

                         inline fun <O> thenThis(crossinline f: R.() -> (
            <#list 1..P1 as I1>Components${I1}<#list 1..I1 as J1>R1${J1}<#if J1 < I1>, </#if></#list><#if I1 < P1>, </#if></#list>,
            <#list 1..P2 as I2>Components${I2}<#list 1..I2 as J2>R2${J2}<#if J2 < I2>, </#if></#list><#if I2 < P2>, </#if></#list>,
            <#list 1..P3 as I3>Components${I3}<#list 1..I3 as J3>R3${J3}<#if J3 < I3>, </#if></#list><#if I3 < P3>, </#if></#list>
          ) -> O): Case<O, R> = StageCase(pat, check) { v -> useComponents(v, f(v)) }
          }

        </#list>
    </#list>
</#list>