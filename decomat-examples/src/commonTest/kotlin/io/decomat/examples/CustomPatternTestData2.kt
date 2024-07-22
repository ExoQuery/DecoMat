package io.decomat.examples

import io.decomat.Components2
import io.decomat.Pattern0
import io.decomat.customPattern2
import io.decomat.examples.customPatternData2.*
import io.decomat.*

// TODO move to commonTest when KT-70025 is fixed
object FirstLast {
  operator fun get(first: Pattern0<String>, last: Pattern0<String>) =
    customPattern2(first, last) { it: Name ->
      on(it).match(
        case(FullName[Is(), Is()]).then { first, last -> Components2(first, last) },
        case(SimpleName[Is(), Is()]).then { first, last -> Components2(first, last) }
      )
    }
}