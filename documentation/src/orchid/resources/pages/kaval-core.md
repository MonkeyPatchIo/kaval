---
---

# Kaval Core

This is the base of Kaval, here there are definition or commons types like `Validator`, `ValidationResult`,
and some commons predefine validators.

## What's a Validator

A `Validator` is function that take a value, and return a `ValidatorResult`.
The `ValidatorResult` could be a `Valid`, or a `Invalid` object.
An the `Invalid` case gather all validation issues.

A `ValidationIssue` always contains a descriptive `message`, and could contain a `field` associated with the issue.

## Validator usages

Imagine we want to validate an integer with an ultimate validator:

```kotlin
val ultimateValidator: Validator<Int> = TODO("See below for creating custom validators")
```

Now we can play with some numbers:

```kotlin
println(ultimateValidator.validate(42))
// Valid

println(ultimateValidator.validate(5))
// Invalid: not the Answer to the Ultimate Question of Life, the Universe, and Everything
```

You also can some extension functions:

```kotlin
42.isValid(ultimateValidator) // true
14.isValid(ultimateValidator) // false

42.isValidOrThrow(ultimateValidator) // 42
24.isValidOrThrow(ultimateValidator) // throw an InvalidException
```

If you want to chain the validation you can do something like that:

```kotlin
val isDigit: Validator<String> = Strings.matches(Regex("^[0-9]$"))

fun parseInt(str: String): Int =
    str.isValidOrThrow(isDigit).toInt()

parseInt("3") // 3: Int

parseInt("foo") // throw an InvalidException
```

You can find more samples [here](../samples/src/main/kotlin)

## Combine Validators

Validators can be combined with `and`, `andThen`, `or`

- `and`: run both validators and gather all issues
- `andThen`: run the first validator, if valid, try the second one. This is a _fail fast_ solution to stop after first issue.
- `or`: run the first validator, if invalid try the second one.

Let's try it with two validators:

```kotlin
val v1 = Strings.maxLength(3)
val v2 = Strings.matches(Regex("^[a-z]*$"))
```

And validate some inputs:

input| `v1 and v2` | `v1 andThen v2` | `v1 or v2`
-----|----------|---------------|---------
`""`|Valid|Valid|Valid
`"abc"`|Valid|Valid|Valid
`"A"`|Invalid: require matching '^[a-z]*$'|Invalid: require matching '^[a-z]*$'|Valid
`"Abcef"`|Invalid:<br> - [length] require to be lower or equals to 3, got 5<br> - require matching '^[a-z]*$'|Invalid: [length] require to be lower or equals to 3, got 5|Invalid:<br> - [length] require to be lower or equals to 3, got 5<br> - require matching '^[a-z]*$'

## Provided validators

### Commons

Into the package `io.monkeypatch.kaval.core`

- `alwaysValid`: an always valid validator, could be useful in test, or when folding a list of Validators.
- `alwaysInvalid(reason: String)`: an always invalid validator, could be useful in test, or when folding a list of Validators.
- `predicate(predicate: (T) -> Boolean, reason: (T) -> String)`: build a validator based on a predicate. Useful to create your own validator.
- `notNull()`: valid when element is not null.
- `containsBy(iter: Iterable<T>)`: valid when element is into the iterable
- `notContainsBy(iter: Iterable<T>)`: valid when element is not into the iterable
- `nullOr(validator: () -> Validator<T>)`: valid if the element is null or if it's valid for the provided sub-validator.
- `field(fieldName: String, fieldExtractor: (H) -> C, fieldValidator: () -> Validator<C>)`: validate a field of an element.
Note that there is a `property` validator define into the `kaval-reflect` module.
- `isInstance<T>()`: validate when element is an instance of a specific type.
- `whenIsInstance(uValidator: () -> Validator<U>)`: allow validation of the element when it's a specific instance.
Useful to validate sealed class. See this [sample](../samples/src/main/kotlin/sealed.kt)

### Strings

Into the object `io.monkeypatch.kaval.core.validator.Strings`

- `notEmpty`: valid for a not empty char sequence.
- `notBlank`: valid for a not blank char sequence.
- `hasLength(length: Int)`: valid when the char sequence has a specific length.
- `maxLength(length: Int)`: valid when the char sequence has a length lower or equals to a specific length.
- `minLength(length: Int)`: valid when the char sequence has a length greater or equals to a specific length.
- `matches(regex: Regex, message: String)`: valid when the char sequence match the regex. The message is optional.

### Comparables

Into the object `io.monkeypatch.kaval.core.validator.Comparables`

- `greaterThan(value: T)`, `greaterThan(value: T, comparator: Comparator<T>)`: valid for when `>`, you can provide a `Comparator`.
- `greaterOrEqualTo(value: T)`, `greaterOrEqualTo(value: T, comparator: Comparator<T>)`: valid for when `>=`, you can provide a `Comparator`.
- `lowerThan(value: T)`, `lowerThan(value: T, comparator: Comparator<T>)`: valid for when `<`, you can provide a `Comparator`.
- `lowerOrEqualTo(value: T)`, `lowerOrEqualTo(value: T, comparator: Comparator<T>)`: valid for when `<=`, you can provide a `Comparator`.
- `equalTo(value: T)`, `equalTo(value: T, comparator: Comparator<T>)`: valid for when `==`, you can provide a `Comparator`.
- `inClosedRange(range: ClosedRange<T>)`: valid for when `in` the range.

### Numbers

Numbers implements `Comparable`, so you can already use methods like `greaterThan`, `lowerOrEqualTo`, ...

Into objects `io.monkeypatch.kaval.core.validator.{Ints, Longs, Floats, Doubles}`

- `inRange(range)`: valid for when `in` the range. The range type depend on the type of validator
- `strictlyPositive`: valid when `>` 0
- `positive`: valid when `>=` 0
- `strictlyNegative`: valid when `<` 0
- `negative`: valid when `<=` 0

### Collections

Into the object `io.monkeypatch.kaval.core.validator.Collections`

- `empty()`: valid if empty
- `notEmpty()`: valid if not empty
- `hasSize(size: Int)`: valid when size has a specific value
- `maxSize(size: Int)`: valid when size is lower or equals to a specific value
- `minSize(size: Int)`: valid when size is lower or equals to a specific value
- `allValid(elementValidator: () -> Validator<T>)`: validate an `Iterable<T>` when all elements are valid. Valid when empty.
- `atLeastOneValid(elementValidator: () -> Validator<T>)`: validate an `Iterable<T>` when at least one element is valid. Invalid if empty.
- `allValuesValid(valueValidator: () -> Validator<V>)`: validate a `Map<K,V>` when all values are valid. Valid when empty.
- `allKeysValid(keyValidator: () -> Validator<K>)`: validate a `Map<K,V>` when all keys are valid. Valid when empty.
- `allEntriesValid(entryValidator: () -> Validator<Map.Entry<K, V>>)`: validate a `Map<K,V>` when all entries are valid. Valid when empty.
- `entryValidator(keyValidator: Validator<K>, valueValidator: Validator<V>)`: validate a `Map.Entry<K,V>` with a key and a value validator.
- `atLeastOneValueValid(valueValidator: () -> Validator<V>)`: validate a `Map<K,V>` when at least one value is valid. Invalid if empty.
- `atLeastOneKeyValid(keyValidator: () -> Validator<K>)`: validate a `Map<K,V>` when at least one key is valid. Invalid if empty.
- `atLeastOneEntryValid(entryValidator: () -> Validator<Map.Entry<K, V>>)`: validate a `Map<K,V>` when at least one entry is valid. Invalid if empty.

## Create your Validator

You can of course combine already existing validators.
It's also quite easy to create your own validator, first we can use the `validator(block: (T) -> ValidationResult)` function:

```kotlin
val ultimateValidator: Validator<Int> = validator { i: Int ->
    if (i == 42) Valid
    else Invalid("not the Answer to the Ultimate Question of Life, the Universe, and Everything")
}
```

Another solution is to use the `predicate` validator:

```kotlin
val ultimateValidator: Validator<Int> =
    predicate({it == 42}) { "not the Answer to the Ultimate Question of Life, the Universe, and Everything"}
```
