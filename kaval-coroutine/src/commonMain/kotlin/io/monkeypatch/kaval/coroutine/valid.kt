package io.monkeypatch.kaval.coroutine

import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationResult
import io.monkeypatch.kaval.core.Validator

/**
 * A [SuspendValidator] is just a suspend function that produce a [ValidationResult]
 *
 * @param T the type of element to be validated
 */
typealias SuspendValidator<T> = suspend (T) -> ValidationResult

/**
 * Just transform a [Validator] to a [SuspendValidator]
 *
 * @param T the type of element to be validated
 * @return the suspend validator
 */
suspend fun <T> Validator<T>.toSuspend(): SuspendValidator<T> =
    { t: T -> this.invoke(t) }

/**
 * Combine two validator in one, like a binary AND
 * A valid element should satisfy both to be valid
 * When an element if invalid for first validator,
 * it will be also validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> SuspendValidator<T>.and(other: Validator<T>): SuspendValidator<T> =
    { t: T ->
        this(t) concat other(t)
    }

/**
 * Combine two validator in one, like a binary AND
 * A valid element should satisfy both to be valid
 * When an element if invalid for first validator,
 * it will be also validate with the second validator.
 *
 * @param other the other suspend validator
 * @return the validator
 */
expect infix fun <T> SuspendValidator<T>.and(other: SuspendValidator<T>): SuspendValidator<T>

/**
 * Combine two validator in one, like a binary AND
 * A valid element should satisfy both to be valid
 * But when an element if invalid for first validator,
 * it will **not** be validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> SuspendValidator<T>.andThen(other: Validator<T>): SuspendValidator<T> =
    { t: T ->
        when (val validation = this(t)) {
            is Valid -> other(t)
            else -> validation
        }
    }

/**
 * Combine two validator in one, like a binary AND
 * A valid element should satisfy both to be valid
 * But when an element if invalid for first validator,
 * it will **not** be validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> SuspendValidator<T>.andThen(other: SuspendValidator<T>): SuspendValidator<T> =
    { t: T ->
        when (val validation = this(t)) {
            is Valid -> other(t)
            else -> validation
        }
    }

/**
 * Combine two validator in one, like a binary OR
 * A valid element should satisfy any of the validator to be valid
 * When an element if valid for first validator,
 * it will **not** be validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> SuspendValidator<T>.or(other: Validator<T>): SuspendValidator<T> =
    { t: T ->
        val tv = this(t)
        if (tv is Valid) Valid
        else {
            val ov = other(t)
            if (ov is Valid) Valid
            else tv concat ov
        }
    }

/**
 * Combine two validator in one, like a binary OR
 * A valid element should satisfy any of the validator to be valid
 * When an element if valid for first validator,
 * it will **not** be validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
expect infix fun <T> SuspendValidator<T>.or(other: SuspendValidator<T>): SuspendValidator<T>
