package io.decomat.examples.custompatterndatay

import io.decomat.*

class FullName_MM<A: Pattern<String>, B: Pattern<String>>(a: A, b: B): Pattern2<A, B, String, String, FullName>(a, b, Typed<FullName>())

object BlahBlah {
  fun harhar(): Pattern2<Pattern0<String>, Pattern0<String>, String, String, Name> {
    val x = io.decomat.examples.querydsl.FlatMap_M(Is(), Is())
    return TODO()
  }
}