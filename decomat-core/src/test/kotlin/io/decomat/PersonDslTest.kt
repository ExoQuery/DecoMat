package io.decomat

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("DANGEROUS_CHARACTERS")
class PersonDslTest: DecomatTest {

  val wrongAnswer = Res2("Wrong", "Answer")

  @Test
  fun `Person(Name("Joe", Is))`() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")]])
          .then { name -> Res1(name) }
      )

    assertEquals(result, Res1(Name("Joe", "Bloggs")))
  }

  @Test
  fun `Person(Name("Joe", Is)) - Decompose`() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")]]).then { (first, last) -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")]]).then { (first, last) -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")]]).then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }

  @Test
  fun `Person(Name("Joe", Is)) - thenIf`() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")]]).then { (first, last) -> wrongAnswer },
        case(Person[Name[Is(), Is()]])
          .thenIf { (first, last) -> last == "Roggs" }
          .then { (first, last) -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")]]).then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }


}