# Kaval kotest

Provide matchers for [kotest](https://github.com/kotest/kotest)

## Provided matchers

### `beValidFor`

Check an element with his validator

```kotlin
val validator: Validator<String> = TODO("pick your validator")
"plop" should beValidFor(validator)
```

### `beValid`

Check a `ValidationResult` is Valid

```kotlin
val validator: Validator<String> = TODO("pick your validator")
validator.validate("plop") should beValid()
```

### `beInvalid`

Check a `ValidationResult` is Invalid

```kotlin
val validator: Validator<String> = TODO("pick your validator")
validator.validate("plop") should beInvalid()
```

### `beInvalidOnField`

Check a `ValidationResult` have an issue on a specific field

```kotlin
val validator: Validator<String> = TODO("pick your validator")
validator.validate("plop") should beInvalidOnField("length")
```

### `beInvalidWithReason`

Check a `ValidationResult` have an issue with a specific message

```kotlin
val validator: Validator<String> = TODO("pick your validator")
validator.validate("plop") should beInvalidWithReason("requires to be not empty")
```

### `beInvalidWithAllReasons`

Check a `ValidationResult` have some issues with specific messages

```kotlin
val validator: Validator<String> = TODO("pick your validator")
validator.validate("plop") should beInvalidWithAllReasons(
    "requires to be not empty",
    "requires matching '^[a-z]*$'"
)
```

### `beInvalidWithAny`

Check a `ValidationResult` have an issue matching a kotest `Matcher`

```kotlin
val validator: Validator<String> = TODO("pick your validator")
validator.validate("plop") should beInvalidWithAny(beLowerCase())
```
