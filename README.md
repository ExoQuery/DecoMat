# Decomat

Scala-Style Deconstructive Pattern-Matching for Kotlin.

Decomat is available on Maven Central. To use it, add the following to your `build.gradle.kts`:
```
implementation("io.exoquery:decomat-core:0.0.2")
ksp("io.exoquery:decomat-ksp:0.0.2")
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

  bigListOfPeople.mapNotNull { p ->
    p.match(
      case( Customer[FullName[Is("Joe"), Is()], Partner[Is()]] )
        .then { first, last, id -> func(first, last, id) },
    )
  }

```



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
    .thenThis { first, last, id ->
      // You can use the `this` keyword to refer to the `Customer` instance (also `this` can be omitted entirely).
      // (the components first, last, id are also available here for convenience)
      this.something + this.somethingElse
    }
  // Other cases...
)
```

##### Is

The `Is(...)` pattern is use to match the innermost patterns. It can be use to match by value, or by type.

Matching by value:
```kotlin
```kotlin
// Match only when affiliate is a Partner with id 123
on(something).match(
  case( Customer[..., Is(Partner(123))] ).then { ... },
  // Other cases...
)
```

Matching by type:
```kotlin
// Match only when affiliate is of the type: Partner
on(something).match(
  case( Customer[..., Is<Partner>] ).then { ... },
  // Other cases...
)
```

It is also possible to use the `Is` pattern to match by a custom predicate. Use this for more complex pattern
matching but be warned that it may be less performant than the other methods because the predicate does not inline.
```kotlin
on(something).match(
  case( Customer[..., Is<Partner> { p -> (p.id == 123 || p.id == 456) }] ).then { ... },
  // Other cases...
)
```

You can also define custom Is-variants based on predicates for example:
```kotlin
def isPartnerWithIds(vararg ids: Int) = IsIs<Partner> { p -> p is Partner && ids.contains(p.id) }
on(something).match(
  case( Customer[..., isPartnerWithIds(123, 456)] ).then { ... },
  // Other cases...
)
```

Note that in many cases, you can use the `thenIf` method instead of the `Is { ... }` predicate function which
does inline leading to better performance.
```kotlin
on(something).match(
  case( Customer[..., Is<Partner>() )
    // This will be inlined
    .thenIf { _, aff -> aff is Partner && (aff.id == 123 || aff.id == 456) }
    .then { ... },
  // Other cases...
)
```

## Custom Patterns

One extremely powerful feature of Scala pattern-matching is that one can use custom patterns in a composable manner.
For example:
```scala
// Create a Data model
case class Person(name: Name, age: Int)
sealed trait Name
case class SimpleName(first: String, last: String) extends Name
case class FullName(first: String, middle: String, last: String) extends Name

// Create a custom pattern
object FirstLast {
  def unapply(name: Name): Option[(String, String)] = name match {
    case SimpleName(first, last) => Some(first, last)
    case FullName(first, _, last) => Some(first, last)
    case _ => None
  }
}

// Now we can use the pattern to match and extract custom data
val p: Person = ...
p match {
  case Person(FirstLast("Joe", last), age) => ...
}
```
Similarly, Decomat allows you to create custom patterns. For example:
```kotlin
// First Create our data model
@Matchable
data class Person(@Component val name: Name, @Component val age: Int): HasProductClass<Person> {
  override val productComponents = ProductClass2(this, name, age)
  companion object { }
}
sealed interface Name
data class SimpleName(val first: String, val last: String): Name
data class FullName(val first: String, val middle: String, val last: String): Name

// Then create our custom pattern matcher. Use the customPattern1 or customPattern2 functions to create the custom pattern.
object FirstLast {
  operator fun get(first: Pattern0<String>, last: Pattern0<String>) =
    customPattern2(first, last) { it: Name ->
      when(it) {
        is SimpleName -> first.matches(it.first) && last.matches(it.last)
        is FullName -> first.matches(it.first) && last.matches(it.last)
        else -> false
      }
    }
}

// Then use the `FirstLast` custom pattern to match and extract data
val p: Person = ...
val out =
  on(p).match(
    case(Person[FirstLast[Is("Joe"), Is()], Is()]).then { (first, last), age -> ... }
  )
```



Note that when Scala pattern matching clauses get complex, it is common to use pattern matching itself in order to deconstruct
patterns into smaller patterns. That means that if we make `SimpleName` and `FullName` matchable, we can
use them with Decomat's matching instead of Kotlin `when` statement. This gives us more versatility.
For example:
```kotlin
// Annotate SimpleName and FullName as @Matchable in additionl to `Person`
@Matchable
data class Person(@Component val name: Name, @Component val age: Int): HasProductClass<Person> {
  override val productComponents = ProductClass2(this, name, age)
  companion object { }
}
sealed interface Name
@Matchable
data class SimpleName(@Component val first: String, @Component val last: String): Name, HasProductClass<SimpleName> {
  override val productComponents = ProductClass2(this, first, last)
  companion object { }
}
@Matchable
data class FullName(@Component val first: String, val middle: String, @Component val last: String): Name, HasProductClass<FullName> {
  override val productComponents = ProductClass3(this, first, middle, last)
  companion object { }
}

// Then create our custom pattern matcher which itself uses on/match functions:
object FirstLast {
  operator fun get(first: Pattern0<String>, last: Pattern0<String>) =
    customPattern2(first, last) { it: Name ->
      on(it).match(
        case(FullName[Is(), Is()]).then { first, last -> Components2(first, last) },
        case(SimpleName[Is(), Is()]).then { first, last -> Components2(first, last) }
      )
    }
}

// Then use the `FirstLast` custom pattern to match and extract data the same as before...
val p: Person = ...
val out =
  on(p).match(
    case(Person[FirstLast[Is("Joe"), Is()], Is()]).then { (first, last), age -> ... }
  )
```

This latter approach is particularly useful when you want the custom pattern matching function itself to have
complex nested conditional logic. For example:
```kotlin
// Match all full-names where the first-name is "Joe" or "Jack"
// Match all simple-names where the last-name is "Bloggs" and "Roogs"
object FirstLast {
  operator fun get(first: Pattern0<String>, last: Pattern0<String>) =
    customPattern2(first, last) { it: Name ->
      on(it).match(
        case(FullName[Is { it == "Joe" || it == "Jack" }, Is()])
          .then { first, last -> Components2(first, last) },
        case(SimpleName[Is(), Is { it == "Bloggs" || it == "Roogs" }])
          .then { first, last -> Components2(first, last) }
      )
    }
}
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

The `Query` interface must be up-casted into into a star-projection when it is used in a match.
```kotlin
val query: Query<Something> = ...
on(query as Query<*>).match(
  case( Map[Is(), Is()] )
    .then { head: Query<*>, body: Query<*> -> func(head, body) },
  case( Entity[Is()] )
    .then { value: Entity<*> -> func(value) },
  // Other cases...
)
```
Note how the `head` and `body` elements are star projections instead of the origin types?
This is done so that the `Map` case can match any `Query` type, otherwise the matching logic would be too restrictive.
(E.g. it would be difficult to deduce the type of the `head` and `body` elements causing the generated code to be incorrect)

If you want to experiment with fully-typed ADT-components nonetheless, use `@Matchable(simplifyTypes = false)`.

## Changing the Annotation Name

Kotlin allows changing an import name using the `import ... as ...` syntax. This can be used to change the
`@Matchable` annotation name to something else, however due to issue [#783](https://github.com/google/ksp/issues/783) it is not possible to genenerically
detect this change inside of a KSP processor. Therefore, if you change the annotation name, you must also
add the following setting to your `build.gradle.kts` file:
```kotlin
// build.gradle.kts
ksp {
    arg("matchableName", "Mat")
    arg("componentName", "Slot")
}
```
Then rename the `@Matchable` annotation to `@Mat` and the `@Component` annotation to `@Slot`
in the import:
```kotlin
import io.decomat.Matchable as Mat
import io.decomat.Component as Slot

// Then use the annotations as follows:
@Mat
data class Person(@Slot val firstName: String, @Slot val lastName: String) {
  override val productComponents = productComponentsOf(this, firstName, lastName)
  companion object {}
}
```


