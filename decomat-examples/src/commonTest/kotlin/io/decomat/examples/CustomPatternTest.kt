package io.decomat.examples

import io.decomat.examples.customPatternData1.*
import io.decomat.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomPatternTest {

  @Test
  fun notMatch1() {
    val p = Person1(SimpleName("John", "Doe"))
    val x: Pattern1<Pattern1<Pattern<String>, String, Name>, Name, Person1> = Person1.get(FirstName2[Is("Jack")])
    val out =
      on(p).match(
        case(Person1[FirstName2[Is("Jack")]]).then { (firstName) -> firstName }
      )

    assertEquals(out, null)
  }


  @Test
  fun notMatch() {
    val p = Person(SimpleName("John", "Doe"), 42)
    val out =
      on(p).match(
        case(Person[FirstName2[Is("Jack")], Is()]).then { (firstName), age -> Pair(firstName, age) }
      )

    assertEquals(out, null)
  }

  @Test
  fun matchOne() {
    val p = Person(SimpleName("John", "Doe"), 42)
    val out1 =
      on(p).match(
        case(Person[FirstName2[Is("John")], Is()]).then { (firstName), age -> Pair(firstName, age) }
      )
    assertEquals(out1, "John" to 42)
  }

  @Test
  fun matchOneAny() {
    val p = Person(SimpleName("John", "Doe"), 42)
    val out1 =
      on(p).match(
        case(Person[FirstName2[Is()], Is()]).then { (firstName), age -> Pair(firstName, age) }
      )
    assertEquals(out1, "John" to 42)
  }
}