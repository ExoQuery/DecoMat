# Decomat

Scala-Style Deconstructive Pattern-Matching for Kotlin.

Decomat is available on Maven Central. To use it, add the following to your `build.gradle.kts`:
```
implementation("io.exoquery:decomat-core:0.0.1")
ksp("io.exoquery:decomat-ksp:0.0.1")
```

## Introduction

Decomat is a library that gives Kotlin a way to do pattern-matching on ADTs (Algebraic Data Types) in a way 
that is similar to Scala's pattern-matching. For example:

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
  case( Customer[Name[Is("Joe"), Is()], Partner[Is()]] )
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
      when (val aff = someone.affiliate) {
        is Partner -> {
          func(someone.name.first, someone.name.last, aff.id)
        }
        else -> fail()
      }
    } else if (someone.name.first == "Jack") {
      when (val aff = someone.affiliate) {
        is Organization -> {
          if (aff.name == "BigOrg") {
            func(someone.name.first, someone.name.last)
          } else fail()
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
* Frequently, other parts of the main object need to be checked during the pattern matching but they do not 
  need to be deconstructed. This can typically be done with a simple `if` statement (see `thenIfThis` below). 

## Tutorial

#### 1. Build
In order to get started with Decomat, add the needed dependencies to your `build.gradle.kts`
file and enable KSP. Decomat relies on various extension methods that are generated by KSP.
```
// build.gradle.kts
plugins {
  ...
  id("com.google.devtools.ksp") version "<ksp-version>"
}

implementation("io.exoquery:decomat-core:<version>")
ksp("io.exoquery:decomat-ksp:<version>")
```

#### 2. Annotate

Then:
* Add the `@Matchable` annotation your class and `@Component` annotation to (up to two) constructor parameters.
* Make the Data Class extend `HasProductClass<YourDataClass>`.
* Add the `productComponents` field to your class and pass `this` and the component-fields into it.
* Add an empty companion-object
```kotlin
@Matchable
data class Customer(@Component val name: Name, @Component val affiliate: Affiliate) {
  override val productComponents = productComponentsOf(this, name, affiliate)
  companion object {}
}
```

Follow these steps for all other classes that you want to pattern match on, in the case above to `Name` and `Partner` as follows:

```kotlin
@Matchable
data class Name(@Component val first: String, @Component val last: String) {
  override val productComponents = productComponentsOf(this, first, last)
  companion object {}
}

@Matchable
data class Partner(@Component val id: Int) {
  override val productComponents = productComponentsOf(this, id)
  companion object {}
}
```

Then use KSP to generate the needed extension methods, in IntelliJ this typically just means
running the 'Rebuild Project' command. The extension-methods will be generated inside of your 
project under `projectDir/build/generated/ksp/main/kotlin/`. They will
be placed in the same package as the `@Matchable` data classes.

> Note that ONLY the parameters that you actually want to deconstruct shuold be annotated with `@Component`
> and only 2 are supported. You can use the `thenIfThis` and `thenThis` methods to conveniently interact
> with non-component methods during filtration.
> There can be other non-component parameters in the constructor before, after, and 
> in-between them:
> ```kotlin
> @Matchable
> data class Customer(
>   val something: String, 
>   @Component val name: Name, 
>   val somethingElse: String, 
>   @Component val affiliate: Affiliate,
>   val yetSomethingElse: String
> ) { ... }
> ```

#### 3. Use!

Then you can use the `on` and `case` functions to pattern match on the ADTs and the `then` function to
perform transformations.
```kotlin
on(someone).match(
  case( Customer[Name[Is("Joe"), Is()], Partner(Is())] )
    .then { first, last, id -> func(first, last, id) },
  // Other cases...
}
```
Note that Scala also allows you to match a variable based on just a type. For example:
```scala
someone match {
  case Customer(Name(first, last), partner: Partner) => func(first, last, part)
}
```
In Decomat, you can do using the the `Is` function with a type and empty parameter-list.
```kotlin
on(someone).match(
  case( Customer[Name[Is(), Is()], Is<Partner>()] )
    // Note how since we are not deconstructing the Partner class anymore, the 3rd parameter
    // switches from the `id: Int` type to the `partner: Partner` type.
    .then { first, last, partner /*: Partner*/ -> func(first, last, partner) },
  // Other cases...
)
```

There are several other methods provided for pattern-matching convenience.

##### thenIf

The `thenIf` method allows you to perform a transformation only if the predicate is true. This is similar
to adding a `if` clause to a Scala pattern-match case. For example:

```scala
someone match {
  case Customer(Name(first, last), Partner(id)) if (first == "Joe") => func(first, last, id)
  ...
}
```
In Decomat, this would be done as follows:
```kotlin
on(someone).match(
  case( Customer[Name[Is(), Is()], Partner(Is())] )
    .thenIf { first, last, id -> first == "Joe" }
    .then { first, last, id -> func(first, last, id) },
  // Other cases...
)
```

##### thenIfThis

If you want to filter by a non `@Component` annoated field, you can use the `thenIfThis` method.
This method allows you to use the pattern-matched object as a reciever. For example:

```kotlin
@Matchable
data class Customer(val something: String, @Component val name: Name, @Component val affiliate: Affiliate) { ... }

on(something).match(
  case( Customer[Name[Is(), Is()], Partner(Is())] )
    .thenIfThis {{ first, last, id ->
      // Note that the first, last, and id properties are available here but you do not necessarily need to use them,
      // since you can use the `this` keyword to refer to the `Customer` instance (also `this` can be omitted entirely).
      something == "something"
    }}
    .then { name, affiliate -> func(name, affiliate) },
  // Other cases...
)
```
Here we are using the `Customer` class as a reciever in the `thenIfThis` class above. The properties
`first`, `last`, and `id` are also available to use if you need them.

> The actual signature of `thenIfThis` is: 
> ```kotlin
> R.() -> (component1, component2, ...) -> Boolean
> ```
> That is why the double braches `{{ ... }}` are needed.

##### thenThis

If you want to use any fields of the pattern-matched object that are not one of the components, you can use the `thenThis` method.
This method allows you to use the pattern-matched object as a reciever. For example:

```kotlin
@Matchable
data class Customer(val something: String, @Component val name: Name, @Component val affiliate: Affiliate) {
  val somethingElse = "somethingElse"
  ...
}

on(something).match(
  case( Customer[Name[Is(), Is()], Partner(Is())] )
    .thenThis {{ first, last, id ->
      // You can use the `this` keyword to refer to the `Customer` instance (also `this` can be omitted entirely).
      // (the components first, last, id are also available here for convenience)
      this.something + this.somethingElse
    }}
  // Other cases...
)
```

## ADTs with Type Parameters (i.e. GADTs)

Decomat supports ADTs with type parameters but they are not used in the Pattern-components. Instead,
they are converted into start-projections. This is because typing all of the parameters would make the
matching highly restrictive. (Also, type-parameters cannot be used as part of the pattern-matching
due to type-erasure.)

For example:
```kotlin
@Metadata
sealed interface Query<T>
data class Map<T, R>(@Component val head: Query<T>, @Component val body: Query<R>): Query<R> {
  // ...
}
data class Entity<T>(@Component val value: T): Query<T> {
  fun <R> someField(getter: () -> R): Query<R> = Property(this, getter())
  // ...
}
```

The `Query` interface is converted into a star-projection when it is used in a match.
```kotlin
on(query).match(
  case( Map[Is(), Is()] )
    .then { head: Query<*>, body: Query<*> -> func(head, body) },
  case( Entity[Is()] )
    .then { value: Entity<*> -> func(value) },
  // Other cases...
)
```
Notw how the `head` and `body` elements are star projections instead of the origin types?
This is done so that the `Map` case can match any `Query` type, otherwise the matching logic would be too restrictive.
(This maybe enhanced in the future.)


## Custom Patterns


