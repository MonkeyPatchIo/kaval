---
---

# Kaval Coroutine

Sometime, your want to write a custom function is `suspend`, you cannot use the standard `Validator<T>`.
This module introduces the `SuspendValidator<T>` validator for _red_ functions.

Further reading: [How do you color your functions?](https://medium.com/@elizarov/how-do-you-color-your-functions-a6bb423d936d)

Note: see [`kaval-core`](../kaval-core) before using the `kaval-coroutine` module.

## Provided SuspendValidator

Into the package `io.monkeypatch.kaval.coroutine`

- `Validator<T>.toSuspend()`: transform a `Validator` to a `SuspendValidator`
- `predicate(predicate: suspend (T) -> Boolean, reason: (T) -> String)`: build a validator based on a predicate.
- `nullOr(validator: suspend () -> Validator<T>)`: valid if the element is null or if it's valid for the provided sub-validator.
- `field(fieldName: String, fieldExtractor: (H) -> C, fieldValidator: suspend () -> Validator<C>)`: validate a field of an element.
- `whenIsInstance(uValidator: suspend () -> Validator<U>)`: allow validation of the element when it's a specific instance.

## Combining SuspendValidator

You can combine validators with:

- `suspendValidator and classicValidator`: just run both validators, and aggregate results
- `suspendValidator and suspendValidator`: run both validators (could be in parallel), and aggregate results
- `suspendValidator andThen classicValidator`: run first validator, and if in success, run the second validator
- `suspendValidator andThen suspendValidator`: run first validator, and if in success, run the second validator
- `suspendValidator or classicValidator`: run both validators, only one success is require to be successful
- `suspendValidator or suspendValidator`: run both validators (could be in parallel), only one success is require to be successful

## Extensions

- `suspend fun <T> T.isValid(validator: SuspendValidator<T>): Boolean`: check if a POJO is valid
- `suspend fun <T> T.isValidOrThrow(validator: SuspendValidator<T>): Boolean`: check if a POJO is valid, otherwise throw an `InvalidException`
