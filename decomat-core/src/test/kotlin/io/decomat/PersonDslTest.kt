package io.decomat

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("DANGEROUS_CHARACTERS")
class PersonDslTest: DecomatTest {

  val wrongAnswer = Res2("Wrong", "Answer")

  @Test
  fun `Person(Name("Joe", "Bloggs" not "Roggs"))`() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")]])
          .then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }

  @Test
  fun `Person(Name(== "Joe", == "Bloggs" not "Roggs"))`() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is {it == "Joe"}, Is {it == "Roggs"}]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is(), Is {it == "Roggs"}]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is {it == "Joe"}, Is {it == "Bloggs"}]])
          .then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }

  @Test
  fun `Person(Name(== "Joe" or "Jack", Is))`() {
    fun isOneOf(vararg ids: String) = Is<String> { ids.contains(it) }
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[isOneOf("Bill", "Will"), Is()]])
          .then { name -> wrongAnswer },
        case(Person[Name[isOneOf("Joe", "Jack"), Is()]])
          .then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
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