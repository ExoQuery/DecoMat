[#ftl]



[#macro Pattern num]
  [@compress single_line=true]
  [#if num == 0]Pattern0<R>
  [#elseif num == 1]Pattern1<P1, R1, R>
  [#elseif num == 2]Pattern2<P1, P2, R1, R2, R>
  [#elseif num == 3]Pattern3<P1, P2, P3, R1, R2, R3, R>
  [#--
  If want to add Pattern 4 etc... can just add a clause here, and then take the This____ generating part and just add one more there
  Don't need to re-do all the macro patterns
  [#elseif num == 4]Pattern3<P1, P2, P3, P4, R1, R2, R3, R4, R>
   --]
  [/#if]
  [/@compress]
[/#macro]

[#--  class Then22<
  P1: Pattern2<P11, P12, R11, R12, R1>,
   P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1,
  P2: Pattern2<P21, P22, R21, R22, R2>,
   P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R>(  --]


[#macro PatternVars mod num]
[@compress single_line=true]
[#if num == 0]Pattern0<R${mod}>, R${mod}
[#elseif num == 1]
  Pattern1<P${mod}1, R${mod}1, R${mod}>, P${mod}1: Pattern<R${mod}1>, R${mod}1, R${mod}
[#elseif num == 2]
  [#-- (P1:) Pattern2<P11, P12, R11, R12, R1>, --]
  Pattern2<P${mod}1, P${mod}2, R${mod}1, R${mod}2, R${mod}>,
  [#-- P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1, --]
  P${mod}1: Pattern<R${mod}1>, R${mod}1,
  [#-- P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, --]
  P${mod}2: Pattern<R${mod}2>, R${mod}2,
  [#-- R1 --]
  R${mod}
[#elseif num == 3]
  [#-- (P1:) Pattern3<P11, P12, P13, R11, R12, R13, R1>, --]
  Pattern3<P${mod}1, P${mod}2, P${mod}3, R${mod}1, R${mod}2, R${mod}3, R${mod}>,
  [#-- P11: Pattern<R11>, R11 --]
  P${mod}1: Pattern<R${mod}1>, R${mod}1,
  [#-- P12: Pattern<R12>, R12 --]
  P${mod}2: Pattern<R${mod}2>, R${mod}2,
  [#-- P13: Pattern<R13>, R13 --]
  P${mod}3: Pattern<R${mod}3>, R${mod}3,
  [#-- R1 --]
  R${mod}
[/#if]
[/@compress]
[/#macro]


[#macro Components mod num]
  [@compress single_line=true]
  [#if num == 0]R${mod}
  [#elseif num == 1]Components1<R${mod}1>
  [#elseif num == 2]Components2<R${mod}1, R${mod}2>
  [#elseif num == 3]Components3<R${mod}1, R${mod}2, R${mod}3>
  [/#if]
  [/@compress]
[/#macro]


[#macro vars mod max]
  [@compress single_line=true]
  [#if max == 0]r${max}
  [#else]([#list 1..max as a]r${mod}${a}[#sep], [/#sep][/#list])
  [/#if]
  [/@compress]
[/#macro]


[#macro compVar mod num]
  [@compress single_line=true]
  [#if num == 0]r${mod}
  [#elseif num == 1]Components1(r${mod}1)
  [#elseif num == 2]Components2(r${mod}1, r${mod}2)
  [#elseif num == 3]Components3(r${mod}1, r${mod}2, r${mod}3)
  [/#if]
  [/@compress]
[/#macro]

[#macro compVars2 i1 i2]
  [@compress single_line=true]
     [@compVar 1 i1 /], [@compVar 2 i2 /]
  [/@compress]
[/#macro]

[#macro compVars3 i1 i2 i3]
  [@compress single_line=true]
     [@compVar 1 i1 /], [@compVar 2 i2 /], [@compVar 3 i3 /]
  [/@compress]
[/#macro]

[#list 0..3 as i1]
  [#list 0..3 as i2]
[#-- fun <P1: Pattern1<P11, R11, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, R11, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then10(pat, {true}) --]

[@output file="decomat-core/build/templates/io/decomat/Then${i1}${i2}.kt"]
package io.decomat

fun <P1: [@PatternVars 1 i1 /], P2: [@PatternVars 2 i2 /], R> case(pat: [@Pattern 2 /]) = Then${i1}${i2}(pat, {true})

class Then${i1}${i2}<P1: [@PatternVars 1 i1 /], P2: [@PatternVars 2 i2 /], R>(
  override val pat: [@Pattern 2 /],
  override val check: (R) -> Boolean
): Stage<[@Pattern 2 /], R> {

  inline fun <O> useComponents(r: R, f: ([@Components 1 i1 /], [@Components 2 i2 /]) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2) = pat.divideIntoComponentsAny(it)
      [#if i1 != 0]val [@vars 1 i1 /] = pat.pattern1.divideIntoComponentsAny(r1 as Any)[#else]//skip[/#if]
      [#if i2 != 0]val [@vars 2 i2 /] = pat.pattern2.divideIntoComponentsAny(r2 as Any)[#else]//skip[/#if]
      f([@compVars2 i1, i2 /])
    } ?: notRightCls(r)

  inline fun <O> then(crossinline f: ([@Components 1 i1 /], [@Components 2 i2 /]) -> O) =
    StageCase(pat, check) { value -> useComponents(value, f) }

  inline fun <O> thenThis(crossinline f: R.() -> ([@Components 1 i1 /], [@Components 2 i2 /]) -> O) =
    StageCase(pat, check) { v -> useComponents(v, f(v)) }
}
[/@output]

  [/#list]
[/#list]


[#list 0..3 as i1]
  [#list 0..3 as i2]
    [#list 0..3 as i3]
[#-- fun <P1: Pattern1<P11, R11, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, R11, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then10(pat, {true}) --]


[@output file="decomat-core/build/templates/io/decomat/Then${i1}${i2}${i3}.kt"]
package io.decomat

fun <P1: [@PatternVars 1 i1 /], P2: [@PatternVars 2 i2 /], P3: [@PatternVars 3 i3 /], R> case(pat: [@Pattern 3 /]) = Then${i1}${i2}${i3}(pat, {true})

class Then${i1}${i2}${i3}<P1: [@PatternVars 1 i1 /], P2: [@PatternVars 2 i2 /], P3: [@PatternVars 3 i3 /], R>(
  override val pat: [@Pattern 3 /],
  override val check: (R) -> Boolean
): Stage<[@Pattern 3 /], R> {

  inline fun <O> useComponents(r: R, f: ([@Components 1 i1 /], [@Components 2 i2 /], [@Components 3 i3 /]) -> O): O =
    (r as? ProductClass<*>)?.let {
      val (r1, r2, r3) = pat.divideIntoComponentsAny(it)
      [#if i1 != 0]val [@vars 1 i1 /] = pat.pattern1.divideIntoComponentsAny(r1 as Any)[#else]//skip[/#if]
      [#if i2 != 0]val [@vars 2 i2 /] = pat.pattern2.divideIntoComponentsAny(r2 as Any)[#else]//skip[/#if]
      [#if i3 != 0]val [@vars 3 i3 /] = pat.pattern3.divideIntoComponentsAny(r3 as Any)[#else]//skip[/#if]
      f([@compVars3 i1, i2, i3 /])
    } ?: notRightCls(r)

  inline fun <O> then(crossinline f: ([@Components 1 i1 /], [@Components 2 i2 /], [@Components 3 i3 /]) -> O) =
    StageCase(pat, check) { value -> useComponents(value, f) }

  inline fun <O> thenThis(crossinline f: R.() -> ([@Components 1 i1 /], [@Components 2 i2 /], [@Components 3 i3 /]) -> O) =
    StageCase(pat, check) { v -> useComponents(v, f(v)) }
}
[/@output]

    [/#list]
  [/#list]
[/#list]