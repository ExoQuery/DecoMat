package io.decomat.examples

import io.decomat.examples.customPatternData2.*
import io.decomat.*
import kotlin.test.Test
import kotlin.test.assertEquals

fun <A: Pattern<AP>, B: Pattern<BP>, AP: Name, BP: Int> Person.Companion.getget(a: A, bb: B) = Person_M(a, bb)

class CustomPatternTest2 {

  @Test
  fun notMatchFirst() {
    val p = Person(SimpleName("John", "Doe"), 42)
    val out =
      on(p).match(
        case(Person.getget(FirstLast.blahblah(Is("Jack"), Is()), Is())).then { (first, last), age -> Triple(first, last, age) }
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