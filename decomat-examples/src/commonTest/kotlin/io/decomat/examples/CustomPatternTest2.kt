// Needs to be commented out due to: https://youtrack.jetbrains.com/issue/KT-70025/KMPKSP-JavaScript-compile-fails-with-Cannot-read-properties-of-undefined
//package io.decomat.examples
//
//import io.decomat.examples.customPatternData2.*
//import io.decomat.*
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class CustomPatternTest2 {
//
//  @Test
//  fun notMatchFirst() {
//    val p = Person(SimpleName("John", "Doe"), 42)
//    val out =
//      on(p).match(
//        case(Person[FirstLast[Is("Jack"), Is()], Is()]).then { (first, last), age -> Triple(first, last, age) }
//      )
//
//    assertEquals(out, null)
//  }
//
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
//
//
//
//}