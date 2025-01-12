package io.decomat.fail

import io.decomat.Typed

fun failToDivide(patName: String, value: kotlin.Any): Nothing =
  fail("Cannot divide $value into components in `$patName`. It is not a product-class.")

fun failToCheck(patName: String, value: kotlin.Any, typed: Typed<*>): Nothing =
  fail("Typecheck for $value failed in `$patName` because it was not a ${typed.cls}.")

fun failToMatch(patName: String, value: kotlin.Any, typed: Typed<*>): Nothing =
  fail("Match for $value failed in `$patName`. (The expected type was ${typed.cls}).")

fun fail(msg: String): Nothing = throw IllegalArgumentException(msg)