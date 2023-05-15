[#ftl]

[#macro generatePattern num mod]
  [@compress]
  [#if num == 0]Pattern0<R${mod}>
  [#elseif num == 1]Pattern1<P${mod}1, R${mod}1, R${mod}>
  [#elseif num == 2]Pattern2<P${mod}1, P${mod}2, R${mod}1, R${mod}2, R${mod}>
  [#elseif num == 3]Pattern3<P${mod}1, P${mod}2, P${mod}3, R${mod}1, R${mod}2, R${mod}3, R${mod}>
  [/#if]
  [/@compress]
[/#macro]

[#--  class Then22<
  P1: Pattern2<P11, P12, R11, R12, R1>,
   P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1,
  P2: Pattern2<P21, P22, R21, R22, R2>,
   P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R>(  --]


[#macro generateFullPattern num mod]
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


[#macro generateComponent num]
  [@compress]
  [#if num == 0]R
  [#elseif num == 1]Components1<R1>
  [#elseif num == 2]Components2<R1, R2>
  [#elseif num == 3]Components3<R1, R2, R3>
  [/#if]
  [/@compress]
[/#macro]

package io.decomat

[#list 0..3 as i1]
  [#list 0..3 as i2]
    [#list 0..3 as i3]
class Then${i1}${i2}${i3}< P1: [@generateFullPattern i1 1 /], P2:[@generateFullPattern i2 2 /], P3: [@generateFullPattern i3 3 /]> {
  inline fun <O> then(crossinline f: ([@generateComponent i1 /], [@generateComponent i2 /], [@generateComponent i3 /]) -> O): OCase<O, R> =
    StageCase(pat, check) { value -> useComponents(value, f) }

  inline fun <O> thenThis(crossinline f: R.() -> ([@generateComponent i1 /], [@generateComponent i2 /], [@generateComponent i3 /]) -> O): Case<O, R> =
    StageCase(pat, check) { v -> useComponents(v, f(v)) }
}
    [/#list]
  [/#list]
[/#list]