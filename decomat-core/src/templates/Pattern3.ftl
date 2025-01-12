[#ftl]


[#macro divideIntoComponentsAnyX num]
    [@compress single_line=true]
    [#if num == 3]divideInto3ComponentsAny
    [#else]divideIntoComponentsAny
    [/#if]
    [/@compress]
[/#macro]

[#macro Pattern num modLeft modRight]
  [@compress single_line=true]
  [#if num == 0]Pattern0<R>
  [#elseif num == 1]Pattern1<[@PatternHeads 1 num /], R1, R>
  [#elseif num == 2]Pattern2<[@PatternHeads 1 modLeft /], [@PatternHeads 2 modRight /], R1, R2, R>
  [#elseif num == 3]Pattern2M<[@PatternHeads 1 modLeft /], M, [@PatternHeads 2 modRight /], R1, R2, R>
  [/#if]
  [/@compress]
[/#macro]

[#--  class Then22<
  P1: Pattern2<P11, P12, R11, R12, R1>,
   P11: Pattern<R11>, P12: Pattern<R12>, R11, R12, R1,
  P2: Pattern2<P21, P22, R21, R22, R2>,
   P21: Pattern<R21>, P22: Pattern<R22>, R21, R22, R2, R>(  --]

[#macro PatternHeads mod num]
[@compress single_line=true]
[#if num == 0]Pattern0<R${mod}>
[#elseif num == 1]
  Pattern1<Pattern<R${mod}1>, R${mod}1, R${mod}>
[#elseif num == 2]
  [#-- Pattern2<Pattern<R11>, Pattern<R12>, R11, R12, R1> --]
  Pattern2<Pattern<R${mod}1>, Pattern<R${mod}2>, R${mod}1, R${mod}2, R${mod}>
[#elseif num == 3]
  [#-- Pattern2M<P11, M, P13, R11, R12, R1> --]
  Pattern2M<Pattern<R${mod}1>, M${mod}, Pattern<R${mod}2>, R${mod}1, R${mod}2, R${mod}>
[/#if]
[/@compress]
[/#macro]


[#macro PatternVars mod num]
  [@compress single_line=true]
  [#if num == 0]R${mod}
  [#elseif num == 1]
    R${mod}1, R${mod}
  [#elseif num == 2]
    [#-- R11, R12, R1 --]
    R${mod}1, R${mod}2, R${mod}
  [#elseif num == 3]
    [#-- R11, M, R12, R1 --]
    R${mod}1, M${mod}, R${mod}2, R${mod}
  [/#if]
  [/@compress]
[/#macro]


[#macro Components mod num]
  [@compress single_line=true]
  [#if num == 0]R${mod}
  [#elseif num == 1]Components1<R${mod}1>
  [#elseif num == 2]Components2<R${mod}1, R${mod}2>
  [#elseif num == 3]Components2M<R${mod}1, M${mod}, R${mod}2>
  [/#if]
  [/@compress]
[/#macro]


[#macro ContextComponents left, right]
  [@compress single_line=true]
  [#if left == 0 && right == 0]Unit
  [#elseif left > 0 && right == 0]ContextComponents1L<R1, R>
  [#elseif left == 0 && right > 0]ContextComponents1R<R2, R>
  [#elseif left > 0 && right > 0]ContextComponents2<R1, R2, R>
  [/#if]
  [/@compress]
[/#macro]

[#macro contextOfContextComponents left, right]
  [@compress single_line=true]
  [#if left == 0 && right == 0]
  [#else]([@ContextComponents left, right /]).
  [/#if]
  [/@compress]
[/#macro]

[#macro fArgs left, right]
  [@compress single_line=true]
  [#if left == 0 && right == 0]c1, c2
  [#else]cc, c1, c2
  [/#if]
  [/@compress]
[/#macro]

[#macro fArgsM left, right]
  [@compress single_line=true]
  [#if left == 0 && right == 0]c1, m, c2
  [#else]cc, c1, m, c2
  [/#if]
  [/@compress]
[/#macro]

[#macro ofContextComponents left, right]
  [@compress single_line=true]
  [#if left == 0 && right == 0]Unit
  [#elseif left > 0 && right == 0]ContextComponents.ofLeft(r1, r)
  [#elseif left == 0 && right > 0]ContextComponents.ofRight(r2, r)
  [#elseif left > 0 && right > 0]ContextComponents.of(r1, r2, r)
  [/#if]
  [/@compress]
[/#macro]


[#macro vars mod max]
  [@compress single_line=true]
  [#if max == 0]r${max}
  [#elseif max == 1 || max == 2]([#list 1..max as a]r${mod}${a}[#sep], [/#sep][/#list])
  [#elseif max == 3](r${mod}1, m${mod}, r${mod}2)
  [#else]
  [/#if]
  [/@compress]
[/#macro]


[#macro compVar mod num]
  [@compress single_line=true]
  [#if num == 0]r${mod}
  [#elseif num == 1]Components1(r${mod}1)
  [#elseif num == 2]Components2(r${mod}1, r${mod}2)
  [#elseif num == 3]Components2M(r${mod}1, m${mod}, r${mod}2)
  [/#if]
  [/@compress]
[/#macro]

[#macro compVars2 i1 i2]
  [@compress single_line=true]
     [@compVar 1 i1 /], [@compVar 2 i2 /]
  [/@compress]
[/#macro]

[#macro compVars2M i1 i2]
  [@compress single_line=true]
     [@compVar 1 i1 /], m, [@compVar 2 i2 /]
  [/@compress]
[/#macro]



[@output file="decomat-core/build/templates/io/decomat/Then2.kt"]
package io.decomat

[#list 0..3 as i1]
  [#list 0..3 as i2]
[#-- fun <P1: Pattern1<P11, R11, R1>, P2: Pattern0<R2>, P11: Pattern<R11>, R11, R1, R2, R> case(pat: Pattern2<P1, P2, R1, R2, R>) = Then10(pat, {true}) --]

[#-- No 'M' variable for these because these are all based on Pattern2, a separate clause is for Pattern2M variants --]
fun <[@PatternVars 1 i1 /], [@PatternVars 2 i2 /], R> case(pat: [@Pattern 2 i1 i2 /]) = Then${i1}${i2}(pat, {true})

[#-- No 'M' variable for these because these are all based on Pattern2, a separate clause is for Pattern2M variants --]
class Then${i1}${i2}<[@PatternVars 1 i1 /], [@PatternVars 2 i2 /], R>(
  override val pat: [@Pattern 2 i1 i2 /],
  override val check: (R) -> Boolean
): Stage<[@Pattern 2 i1 i2 /], R> {

  inline fun <O> useComponents(r: R, f: ([@ContextComponents i1, i2 /], [@Components 1 i1 /], [@Components 2 i2 /]) -> O): O {
    val (r1, r2) = pat.divideIntoComponentsAny(r as Any)
    [#if i1 != 0]val [@vars 1 i1 /] = pat.pattern1.[@divideIntoComponentsAnyX i1 /](r1 as Any)[#else]//skip[/#if]
    [#if i2 != 0]val [@vars 2 i2 /] = pat.pattern2.[@divideIntoComponentsAnyX i2 /](r2 as Any)[#else]//skip[/#if]
    return f([@ofContextComponents i1, i2 /], [@compVars2 i1, i2 /])
  }
  inline fun thenIf(crossinline f: [@contextOfContextComponents i1, i2 /]([@Components 1 i1 /], [@Components 2 i2 /]) -> Boolean) =
    Then${i1}${i2}(pat) { v -> check(v) && useComponents(v, { cc, c1, c2 -> f([@fArgs i1 i2 /]) }) }

  inline fun thenIfThis(crossinline f: R.([@Components 1 i1 /], [@Components 2 i2 /]) -> Boolean) =
    Then${i1}${i2}(pat) { v -> check(v) && useComponents(v, { _, c1, c2 -> f(v, c1, c2) }) }

  inline fun <O> then(crossinline f: [@contextOfContextComponents i1, i2 /]([@Components 1 i1 /], [@Components 2 i2 /]) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { cc, c1, c2 -> f([@fArgs i1 i2 /]) }) }

  inline fun <O> thenThis(crossinline f: R.([@Components 1 i1 /], [@Components 2 i2 /]) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { _, c1, c2 -> f(v, c1, c2) }) }
}
  [/#list]
[/#list]

[#-- ******************************************************************************************************* --]
[#-- ***************************** The Variants that are Pattern2M at the base ***************************** --]
[#-- ******************************************************************************************************* --]

[#list 0..3 as i1]
  [#list 0..3 as i2]

fun <[@PatternVars 1 i1 /], M, [@PatternVars 2 i2 /], R> case(pat: [@Pattern 3 i1 i2 /]) = Then${i1}M${i2}(pat, {true})

class Then${i1}M${i2}<[@PatternVars 1 i1 /], M, [@PatternVars 2 i2 /], R>(
  override val pat: [@Pattern 3 i1 i2 /],
  override val check: (R) -> Boolean
): Stage<[@Pattern 3 i1 i2 /], R> {

  inline fun <O> useComponents(r: R, f: ([@ContextComponents i1, i2 /], [@Components 1 i1 /], M, [@Components 2 i2 /]) -> O): O {
    val (r1, m, r2) = pat.divideInto3ComponentsAny(r as Any)
    [#if i1 != 0]val [@vars 1 i1 /] = pat.pattern1.[@divideIntoComponentsAnyX i1 /](r1 as Any)[#else]//skip[/#if]
    [#if i2 != 0]val [@vars 2 i2 /] = pat.pattern2.[@divideIntoComponentsAnyX i2 /](r2 as Any)[#else]//skip[/#if]
    return f([@ofContextComponents i1, i2 /], [@compVars2M i1, i2 /])
  }
  inline fun thenIf(crossinline f: [@contextOfContextComponents i1, i2 /]([@Components 1 i1 /], M, [@Components 2 i2 /]) -> Boolean) =
    Then${i1}M${i2}(pat) { v -> check(v) && useComponents(v, { cc, c1, m, c2 -> f([@fArgsM i1 i2 /]) }) }

  inline fun thenIfThis(crossinline f: R.([@Components 1 i1 /], M, [@Components 2 i2 /]) -> Boolean) =
    Then${i1}M${i2}(pat) { v -> check(v) && useComponents(v, { _, c1, m, c2 -> f(v, c1, m, c2) }) }

  inline fun <O> then(crossinline f: [@contextOfContextComponents i1, i2 /]([@Components 1 i1 /], M, [@Components 2 i2 /]) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { cc, c1, m, c2 -> f([@fArgsM i1 i2 /]) }) }

  inline fun <O> thenThis(crossinline f: R.([@Components 1 i1 /], M, [@Components 2 i2 /]) -> O) =
    StageCase(pat, check) { v -> useComponents(v, { _, c1, m, c2 -> f(v, c1, m, c2) }) }
}
  [/#list]
[/#list]


[/@output]