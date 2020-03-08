package io.monkeypatch.kaval.core

/**
 * Check an element is valid (Predicate)
 *
 * @param T the element type
 * @return `true` if valid, `false` otherwise
 */
fun <T> T.isValid(validator: Validator<T>): Boolean =
    validator.validate(this) == Valid

/**
 * Check an element is valid, throw an [InvalidException] if invalid
 *
 * @param T the element type
 * @throws InvalidException when element is invalid
 * @return the element if valid, otherwise throw an [InvalidException]
 */
fun <T> T.isValidOrThrow(validator: Validator<T>): T =
    when (val result = validator.validate(this)) {
        is Valid -> this
        is Invalid -> throw InvalidException(result)
    }

/**
 * Occurs when an element is invalid
 *
 * @param invalid the validation result
 */
data class InvalidException(val invalid: Invalid) : RuntimeException(
    if (invalid.reasons.size <= 1) invalid.reasons[0].toString()
    else invalid.reasons.joinToString("\n")
)
