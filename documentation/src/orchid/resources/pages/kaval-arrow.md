---
---

# Kaval Arrow

[ArrowKt](https://arrow-kt.io) is an awesome Kotlin functional library,
and this module provide some integration with this library.

## Arrow Validated vs Kaval validation

The Kaval approach is to provide an easy way to write and combine some validator, it's just a DSL.
On the other side, the `Validated` is based on the type category,
and implements some HKT like __Functor__, __Applicative__, __Traverse__, ...

Although these are two different approaches of validation, they are not in competition.
In my opinion, the idea would be to use Kaval as a DSL to easily declare validation rules,
and then if we need Arrow's ad hoc polymorphism,
to transform Kaval validators to provide `Validated` or `Either`.

Another noteworthy point is that Kaval is intended to be multi-platform.

## Provided Validators

### `Option<T>`

- `isNoneOr(validator: () -> Validator<C>)`: accept a `None`, or a valid `Some`
- `isNone()`: require a `None`, probably not very useful
- `isSome()`: require a `Some`, probably not very useful

### `Either<L, R>`

- `validator(leftValidator: () -> Validator<L>, rightValidator: () -> Validator<R>)`: accept a valid `Either.Left`,
  or a valid `Either.Right`
- `isRight()`: require an `Either.Right`
- `isRight(validator: () -> Validator<R>)`: require a valid `Either.Right`
- `isLeft()`: require an `Either.Left`
- `isLeft(validator: () -> Validator<L>)`: require an `Either.Left`

### `Ior<L, R>`

- `validator(leftValidator: () -> Validator<L>, rightValidator: () -> Validator<R>)`: accept a valid `Ior.Left`,
  or a valid `Ior.Right`, or a valid `Ior.Both`
- `isRight()`: require an `Ior.Right`
- `isRight(validator: () -> Validator<R>)`: require a valid `Ior.Right`
- `isLeft()`: require an `Ior.Left`
- `isLeft(validator: () -> Validator<L>)`: require a valid `Ior.Left`
- `isBoth()`: require an `Ior.Both`
- `isLeft(leftValidator: () -> Validator<L>, rightValidator: () -> Validator<R>)`: require a valid `Ior.Both`

### `NonEmptyList<T>`

- `hasSize(size: Int)`: valid when size has a specific value
- `maxSize(size: Int)`: valid when size is lower or equals to a specific value
- `minSize(size: Int)`: valid when size is lower or equals to a specific value
- `allValid(elementValidator: () -> Validator<T>)`: valid when all elements are valid.
- `atLeastOneValid(elementValidator: () -> Validator<T>)`: valid when at least one element is valid.

## Validation returning a Validated

The `KavalValidated` object provide some useful extension if you want to return an Arrow `Validated`.

A `ValidationResult` is isomorphic to `Validated<Nel<ValidationIssue>, Unit>`, you can use the
`ValidationResult.toValidated(): Validated<Nel<ValidationIssue>, Unit>` extension function
to map a validation result to an Arrow `Validated`.

The `Validator<T>.validateValidated(t: T): Validated<Nel<ValidationIssue>, T>` extension function can be used
to return the Arrow `Validated`.

And you can check an object with a validator like that:

```kotlin
// Put the right check method into the scope
io.monkeypatch.kaval.arrow.KavalValidated.check

// A successful case
val addressOk = Address(
    line1 = "42 avenue Monoid",
    line2 = "Semigroup block",
    zipCode = 42000,
    city = "LambdaCity"
)

println(addressOk.check(Address.validator))
// Valid(Address(line1=42 avenue Monoid, line2=Semigroup block, zipCode=42000, city=LambdaCity))

// And a failure case
val addressKo = Address(
    line1 = " ".repeat(500),
    line2 = "",
    zipCode = -1,
    city = ""
)

println(addressKo.check(Address.validator))
// (indented)
// Invalid(
//   NonEmptyList([
//     [line1] requires to be not blank,
//     [line1.length] requires to be lower or equals to 255, got 500,
//     [zipCode] requires to be greater than 0, got -1,
//     [city] requires to be not blank
//   ])
// )
```

## Validation returning an Either

The `KavalEither` object provide some useful extension if you perfer returning an Arrow `Either`.

A `ValidationResult` is isomorphic to `Either<InvalidException, Unit>`, you can use the
`ValidationResult.toEither(): Either<InvalidException, Unit>` extension function
to map a validation result to an Arrow `Validated`.

The `Validator<T>.validateEither(t: T): Either<InvalidException, T>` extension function can be used
to return the Arrow `Validated`.

And you can check an object with a validator like that:

```kotlin
// Put the right check method into the scope
io.monkeypatch.kaval.arrow.KavalEither.check

// A successful case
val addressOk = Address(
    line1 = "42 avenue Monoid",
    line2 = "Semigroup block",
    zipCode = 42000,
    city = "LambdaCity"
)

println(addressOk.check(Address.validator))
// Right(Address(line1=42 avenue Monoid, line2=Semigroup block, zipCode=42000, city=LambdaCity))

// And a failure case
val addressKo = Address(
    line1 = " ".repeat(500),
    line2 = "",
    zipCode = -1,
    city = ""
)

println(addressKo.check(Address.validator))
// Left(InvalidException(invalid=Invalid:
//  - [line1] requires to be not blank
//  - [line1.length] requires to be lower or equals to 255, got 500
//  - [zipCode] requires to be greater than 0, got -1
//  - [city] requires to be not blank))
```
