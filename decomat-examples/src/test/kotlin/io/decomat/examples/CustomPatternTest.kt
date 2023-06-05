package io.decomat.examples

import io.decomat.examples.customPatternData1.*
import io.decomat.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomPatternTest {

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