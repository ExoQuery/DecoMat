package io.decomat.fail

fun failToDivide(value: kotlin.Any): Nothing =
  fail("Cannot divide $value into components. It is not a product-class.")

fun fail(msg: String): Nothing = throw IllegalArgumentException(msg)