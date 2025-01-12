package io.decomat.fail

import io.decomat.Typed

fun failToDivide(value: kotlin.Any): Nothing =
  fail("Cannot divide $value into components. It is not a product-class.")

fun failToCheck(value: kotlin.Any, typed: Typed<*>): Nothing =
  fail("Typecheck for $value failed because it was not a ${typed.cls}.")

fun failToMatch(value: kotlin.Any, typed: Typed<*>): Nothing =
  fail("Match for $value failed. (The expected type was ${typed.cls}).")

fun fail(msg: String): Nothing = throw IllegalArgumentException(msg)