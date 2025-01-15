package io.decomat.examples

import io.decomat.Components1
import io.decomat.Pattern0
import io.decomat.customPattern1
import io.decomat.examples.customPatternData1.*

// TODO move to commonTest when KT-70025 is fixed
object FirstName2 {
  operator fun get(nameString: Pattern0<String>) =
    customPattern1("FirstName2", nameString) { it: Name ->
      when(it) {
        is SimpleName -> Components1(it.first)
        is FullName -> Components1(it.first)
      }
    }
}
