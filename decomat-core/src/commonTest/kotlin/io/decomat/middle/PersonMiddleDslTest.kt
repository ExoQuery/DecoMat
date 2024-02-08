package io.decomat.middle

import io.decomat.*
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("DANGEROUS_CHARACTERS")
class PersonMiddleDslTest: DecomatTest {

  val wrongAnswer = Res2("Wrong", "Answer")

  // Person(Name("Joe", "Bloggs" not "Roggs"))
  @Test
  fun person_name_joe_bloggs_not_roggs() {
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

  // Person(Name("Joe" not "Jack", "Bloggs"))
  @Test
  fun person_name_joe_not_jack_bloggs() {
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

  // Person(Name(== "Joe", == "Bloggs" not "Roggs"))
  @Test
  fun person_name_joe_bloggs_not_roggs2() {
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

  // Person(Name(== "Joe" or "Jack", Is))
  @Test
  fun person_name_joe_or_jack_is() {
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

  // Person(Name(== "Joe" or "Jack", Is))
  @Test
  fun person_name_joe_or_jack_is2() {
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


  // Person(Name("Joe", Is)) - thenIf - Positive
  @Test
  fun person_name_joe_is_thenIf_positive() {
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

  // Person(Name("Joe", Is)) - thenIf - Negative
  @Test
  fun person_name_joe_is_thenIf_negative() {
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