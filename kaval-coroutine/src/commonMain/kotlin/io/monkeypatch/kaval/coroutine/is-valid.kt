package io.monkeypatch.kaval.coroutine

import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.InvalidException
import io.monkeypatch.kaval.core.Valid

/**
 * Check an element is valid (Predicate)
 *
 * @param validator the suspendable validator
 * @param T the element type
 * @return `true` if valid, `false` otherwise
 */
suspend fun <T> T.isValid(validator: SuspendValidator<T>): Boolean =
    validator(this) == Valid

/**
 * Check an element is valid, throw an [InvalidException] if invalid
 *
 * @param validator the suspendable validator
 * @param T the element type
 * @throws InvalidException when element is invalid
 * @return the element if valid, otherwise throw an [InvalidException]
 */
suspend fun <T> T.isValidOrThrow(validator: SuspendValidator<T>): T =
    when (val result = validator(this)) {
        is Valid -> this
        is Invalid -> throw InvalidException(result)
    }
