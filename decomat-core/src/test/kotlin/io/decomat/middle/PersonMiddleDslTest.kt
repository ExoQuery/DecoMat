package io.decomat.middle

import io.decomat.*
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("DANGEROUS_CHARACTERS")
class PersonMiddleDslTest: DecomatTest {

  val wrongAnswer = Res2("Wrong", "Answer")

  @Test
  fun `Person(Name("Joe", "Bloggs" not "Roggs"))`() {
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")], Is()])
          .then { (a, b, c), d, e -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")], Is()])
          .then { (a, b, c), d, e -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")], Is()])
          .then { (a, b, c), d, e -> Res5(a, b, c, d, e) }
      )

    assertEquals(result, Res5("Joe", 11, "Bloggs", "Jr", 123))
  }

  @Test
  fun `Person(Name("Joe" not "Jack", "Bloggs"))`() {
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[Is(), Is("Roggs")], Is()])
          .then { (a, b, c), d, e -> wrongAnswer },
        case(Person[Name[Is("Jack"), Is()], Is()])
          .then { (a, b, c), d, e -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")], Is()])
          .then { (a, b, c), d, e -> Res5(a, b, c, d, e) }
      )

    assertEquals(result, Res5("Joe", 11, "Bloggs", "Jr", 123))
  }

  @Test
  fun `Person(Name(== "Joe", == "Bloggs" not "Roggs"))`() {
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[Is {it == "Joe"}, Is {it == "Roggs"}], Is()])
          .then { n, m, a -> wrongAnswer },
        case(Person[Name[Is(), Is {it == "Roggs"}], Is()])
          .then { n, m, a -> wrongAnswer },
        case(Person[Name[Is {it == "Joe"}, Is {it == "Bloggs"}], Is()])
          .then { (first, n1, last), suffix, age -> Res3(Res3(first, n1, last), suffix, age) }
      )

    assertEquals(result, Res3(Res3("Joe", 11, "Bloggs"), "Jr", 123))
  }

  @Test
  fun `Person(Name(== "Joe" or "Jack", Is))`() {
    fun isOneOf(vararg ids: String) = Is<String> { ids.contains(it) }
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[isOneOf("Bill", "Will"), Is()], Is()])
          .then { n, m, a -> wrongAnswer },
        case(Person[Name[isOneOf("Joe", "Jack"), Is()], Is()])
          .then { (first, n1, last), suffix, age -> Res5(first, n1, last, suffix, age) }
      )

    assertEquals(result, Res5("Joe", 11, "Bloggs", "Jr", 123))
  }

  @Test
  fun `Person(Name("Joe", Is)) - Decompose`() {
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")], Is()])
          .then { (first, n1, last), suffix, age -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")], Is()])
          .then { (first, n1, last), suffix, age -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")], Is(123)])
          .then { (first, n1, last), suffix, age -> Res5(first, n1, last, suffix, age) }
      )

    assertEquals(result, Res5("Joe", 11, "Bloggs", "Jr", 123))
  }

  @Test
  fun `Person(Name("Joe", Is)) - thenIf - Positive`() {
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")], Is()])
          .then { (first, n1, last), suffix, age -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")], Is()])
          .thenIf { (first, n1, last), suffix, age -> last == "Roggs" }
          .then { (first, n1, last), suffix, age -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")], Is()])
          .thenIf { (first, n1, last), suffix, age -> last == "Bloggs" }
          .then { (first, n1, last), suffix, age -> Res5(first, n1, last, suffix, age) }
      )

    assertEquals(result, Res5("Joe", 11, "Bloggs", "Jr", 123))
  }

  @Test
  fun `Person(Name("Joe", Is)) - thenIf - Negative`() {
    val result =
      on(Person(Name("Joe", 11, "Bloggs"), "Jr", 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")], Is()])
          .then { (first, n1, last), suffix, age -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")], Is()])
          .thenIf { (first, n1, last), suffix, age -> last == "Roggs" }
          .then { (first, n1, last), suffix, age -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")], Is()])
          .then { (first, n1, last), suffix, age -> Res5(first, n1, last, suffix, age) }
      )

    assertEquals(result, Res5("Joe", 11, "Bloggs", "Jr", 123))
  }




}