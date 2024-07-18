package io.decomat.examples

import io.decomat.examples.custompatterndatay.*
import io.decomat.*
import io.decomat.examples.custompatterndatay.FirstLast
import io.decomat.examples.custompatterndatay.Name
import io.decomat.examples.custompatterndatay.Person
import io.decomat.examples.custompatterndatay.SimpleName
import kotlin.test.Test

fun <A: Pattern<AP>, B: Pattern<BP>, AP: Name, BP: Int> Person.Companion.getget(a: A, bb: B) = Person_M(a, bb)

class CustomPatternTest2 {

  /*
  Are the JS errors happening perhaps because we have AP:String which JS does not expect?
  this is happening specifically for customPatternData2 i.e. FirstName generated classes and only
  when they are in a generated file
   */
  @Test
  fun notMatchFirst() {
    val p = Person(SimpleName("John", "Doe"), 42)

    //val x = io.decomat.examples.querydsl.FlatMap_M(Is(), Is())
    val out =
      on(p).match(
        case(Person.getget(BlahBlah.harhar(), Is())).then { (first, last), age -> Triple(first, last, age) }
        //case(Person[Is(), Is()]).then { a, b -> "foo" }
      )

    //assertEquals(out, null)
  }

//  @Test
//  fun notMatchSecond() {
//    val p = Person(SimpleName("John", "Doe"), 42)
//    val out =
//      on(p).match(
//        case(Person[FirstLast[Is(), Is("Daves")], Is()]).then { (first, last), age -> Triple(first, last, age) }
//      )
//
//    assertEquals(out, null)
//  }
//
//  @Test
//  fun notMatchBoth() {
//    val p = Person(SimpleName("John", "Doe"), 42)
//    val out =
//      on(p).match(
//        case(Person[FirstLast[Is("Jack"), Is("Daves")], Is()]).then { (first, last), age -> Triple(first, last, age) }
//      )
//
//    assertEquals(out, null)
//  }
//
//  @Test
//  fun matchTwoFirst() {
//    val p = Person(SimpleName("John", "Doe"), 42)
//    val out1 =
//      on(p).match(
//        case(Person[FirstLast[Is("John"), Is("Doe")], Is()]).then { (first, last), age -> Triple(first, last, age) }
//      )
//    assertEquals(out1, Triple("John", "Doe", 42))
//  }
//
//  @Test
//  fun matchTwo() {
//    val p = Person(SimpleName("John", "Doe"), 42)
//    val out1 =
//      on(p).match(
//        case(Person[FirstLast[Is("John"), Is()], Is()]).then { (first, last), age -> Triple(first, last, age) }
//      )
//    assertEquals(out1, Triple("John", "Doe", 42))
//  }
//
//  @Test
//  fun matchTwoAny() {
//    val p = Person(SimpleName("John", "Doe"), 42)
//    val out1 =
//      on(p).match(
//        case(Person[FirstLast[Is(), Is()], Is()]).then { (first, last), age -> Triple(first, last, age) }
//      )
//    assertEquals(out1, Triple("John", "Doe", 42))
//  }



}