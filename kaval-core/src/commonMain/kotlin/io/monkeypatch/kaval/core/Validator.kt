package io.monkeypatch.kaval.core

/**
 * A validator
 *
 * @param T the type of element to be validated
 */
interface Validator<in T> {

    /**
     * Validate an element
     *
     * @param t the element to validate
     * @return the validation result
     */
    fun <U : T> validate(t: U): ValidationResult

    /**
     * Combine two validator in one, like a binary AND
     * A valid element should satisfy both to be valid
     * When an element if invalid for first validator,
     * it will be also validate with the second validator.
     *
     * @param other the other validator
     * @return the validator
     */
    infix fun and(other: Validator<@UnsafeVariance T>): Validator<T> =
        validator { t: T ->
            this.validate(t) concat other.validate(t)
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
    infix fun andThen(other: Validator<@UnsafeVariance T>): Validator<T> =
        validator { t: T ->
            when (val validation = this.validate(t)) {
                is Valid -> other.validate(t)
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
    infix fun or(other: Validator<@UnsafeVariance T>): Validator<T> =
        validator { t: T ->
            val tv = this.validate(t)
            if (tv is Valid) Valid
            else {
                val ov = other.validate(t)
                if (ov is Valid) Valid
                else tv concat ov
            }
        }
}

/**
 * Create a validator from a lambda
 *
 * @param block a function that take an element, and return the validation result
 * @return the validator
 */
fun <T> validator(block: (T) -> ValidationResult): Validator<T> =
    object : Validator<T> {
        override fun <U : T> validate(t: U): ValidationResult = block(t)
    }
