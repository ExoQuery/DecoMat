package io.decomat

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("DANGEROUS_CHARACTERS")
class PersonDslTest: DecomatTest {

  val wrongAnswer = Res2("Wrong", "Answer")

  // Person(Name("Joe", "Bloggs" not "Roggs"))
  @Test
  fun person_name_joe_bloggs_not_roggs() {
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

  // Testing early-exist case
  @Test
  fun person_name_joe_bloggs_not_roggs_early() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")]])
          .then { name -> wrongAnswer },
        caseEarly(false)(Person[Name[Is("Joe"), Is("Bloggs")]])
          .then { name -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")]])
          .then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }

  @Test
  fun name_joe_bloggs_not_roggs_early() {
    val result =
      on(Name("Joe", "Bloggs")).match(
        case(Name[Is("Joe"), Is("Roggs")])
          .then { _, _ -> wrongAnswer },
        case(Name[Is(), Is("Roggs")])
          .then { _, _ -> wrongAnswer },
        caseEarly(false).invoke(Name[Is("Joe"), Is("Bloggs")])
          .then { _, _ -> wrongAnswer },
        case(Name[Is("Joe"), Is("Bloggs")])
          .then { first, last -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }

  // Person(Name(== "Joe", == "Bloggs" not "Roggs"))
  @Test
  fun person_name_joe_bloggs_not_roggs1() {
    Person(Name("Joe", "Bloggs"), 123).match(
      //fastCase("foo" == "foo")(Name[Is(), Is()]).then {
      //
      //}
      case(Person[Name[Is { it == "Joe" }, Is { it == "Roggs" }]])
        .then { name -> wrongAnswer },
      case(Person[Name[Is(), Is { it == "Roggs" }]])
        .then { name -> wrongAnswer },
      case(Person[Name[Is { it == "Joe" }, Is { it == "Bloggs" }]])
        .then { (first, last) -> Res2(first, last) }
    )
  }

    @Test
  fun person_name_joe_bloggs_not_roggs_2() {
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

  // Person(Name(== "Joe" or "Jack", Is))
  @Test
  fun person_name_joe_or_jack_is() {
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

  // Person(Name("Joe", Is)) - Deconstruct
  @Test
  fun person_name_joe_is__deconstruct() {
    val result =
      on(Person(Name("Joe", "Bloggs"), 123)).match(
        case(Person[Name[Is("Joe"), Is("Roggs")]]).then { (first, last) -> wrongAnswer },
        case(Person[Name[Is(), Is("Roggs")]]).then { (first, last) -> wrongAnswer },
        case(Person[Name[Is("Joe"), Is("Bloggs")]]).then { (first, last) -> Res2(first, last) }
      )

    assertEquals(result, Res2("Joe", "Bloggs"))
  }

  // Person(Name("Joe", Is)) - thenIf
  @Test
  fun person_name_joe_is__thenIf() {
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