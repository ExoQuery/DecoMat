# Decomat

Scala-Style Deconstructive Pattern-Matching for Kotlin.

Decomat is available on Maven Central. To use it, add the following to your `build.gradle.kts`:
```
implementation("io.exoquery:decomat-core:0.0.1")
ksp("io.exoquery:decomat-ksp:0.0.1")
```

## Introduction

Decomat is a library that gives Kotlin a way to do pattern-matching on ADTs (Algebraic Data Types) in a way similar 
to Scala's pattern-matching. For example:

```scala
case class Customer(name: Name, affiliate: Affiliate)
case class Name(first: String, last: String)
sealed trait Affiliate
case class Partner(id: Int) extends Affiliate
case class Organization(name: String) extends Affiliate

someone match {
  case Customer(Name(first @ "Joe", last), Partner(id)) => func(first, last, id)
  case Customer(Name(first @ "Jack", last), Organization("BigOrg")) => func(first, last)
}
```

Similarly, in Kotlin with Decomat you can do this:
```kotlin
on(someone).match(
  case( Customer[Name[Is("Joe"), Is()], Partner(Is())] )
    .then { first, last, id -> func(first, last, id) },
  case( Customer[Name[Is("Jack"), Is()], Organization[Is("BigOrg")]] )
    .then { first, last -> func(first, last) }
)
```

Whereas normally the following would be needed:
```kotlin
when(someone) {
  is Customer ->
    if (someone.name.first == "Joe") {
      val first = someone.name.first
      val last = someone.name.last
      when (someone.affiliate) {
        is Partner -> {
          val id = someone.affiliate.id
          func(first, last, id)
        }
        else -> fail()
      }
    } else if (someone.name.first == "Jack") {
      val first = someone.name.first
      val last = someone.name.last
      when (someone.affiliate) {
        is Organization -> {
          func(first, last)
        }
        else -> fail()
      }
    } else fail()
}
```

Decomat is not a full replacement of Scala's pattern-matching, but it does have some of the same 
features and usage patterns in a limited scope. The primary insight behind Decomat is that for in most of 
the cases where Scala ADT pattern matching is used:

* No more than two components need to be deconstructed (3 will be partially supported soon)
* The deconstruction itself does not need to be more than two levels deep
* The components that need to be deconstructed are usually known as the ADT case-classes are being written.

## Getting Started

TBD

## A Note about GADTs (i.e. ADTs with type parameters)

TBD



