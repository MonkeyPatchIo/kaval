package io.monkeypatch.kaval.core

/**
 * A [Validator] is just a function that produce a [ValidationResult]
 *
 * @param T the type of element to be validated
 */
typealias Validator<T> = (T) -> ValidationResult

/**
 * Combine two validator in one, like the binary AND operation
 * A valid element should satisfy both to be valid.
 * When an element if invalid for first validator,
 * it will be also validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> Validator<T>.and(other: Validator<T>): Validator<T> =
    { t ->
        this(t) concat other(t)
    }

/**
 * Combine two validator in one, like the binary AND operation
 * A valid element should satisfy both to be valid
 * But when an element if invalid for first validator,
 * it will **not** be validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> Validator<T>.andThen(other: Validator<T>): Validator<T> =
    { t ->
        when (val validation = this(t)) {
            is Valid -> other(t)
            else -> validation
        }
    }

/**
 * Combine two validator in one, like the binary OR operation
 * A valid element should satisfy any of the validator to be valid
 * When an element if valid for first validator,
 * it will **not** be validate with the second validator.
 *
 * @param other the other validator
 * @return the validator
 */
infix fun <T> Validator<T>.or(other: Validator<T>): Validator<T> =
    { t: T ->
        val tv = this(t)
        if (tv is Valid) Valid
        else {
            val ov = other(t)
            if (ov is Valid) Valid
            else tv concat ov
        }
    }
