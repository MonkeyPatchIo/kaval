package io.monkeypatch.kaval.arrow

import arrow.core.Nel
import arrow.core.Validated
import arrow.core.invalid
import arrow.core.nel
import arrow.core.valid
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationIssue
import io.monkeypatch.kaval.core.ValidationResult
import io.monkeypatch.kaval.core.Validator

object KavalValidated {

    /**
     * A [ValidationResult] is isomorphic to [Validated]
     *
     * @return an `Validated<Nel<ValidationIssue>, Unit>`
     */
    fun ValidationResult.toValidated(): Validated<Nel<ValidationIssue>, Unit> =
        when (this) {
            is Valid -> Unit.valid()
            is Invalid -> {
                val head = issues.first()
                val tail = issues.drop(1)
                (head.nel() + tail).invalid()
            }
        }

    /**
     * Validate and produce a [Validated]
     *
     * @param t the element
     * @param T the element type
     * @return an `Validated<Nel<ValidationIssue>, T>`
     */
    fun <T> Validator<T>.validateValidated(t: T): Validated<Nel<ValidationIssue>, T> =
        this.validate(t)
            .toValidated()
            .map { t }

    /**
     * Check an element is valid
     *
     * @param T the element type
     * @return an [Validated.Valid] with the element if valid, otherwise an [Validated.Invalid] with a not empty list of [ValidationIssue]
     */
    fun <T> T.check(validator: Validator<T>): Validated<Nel<ValidationIssue>, T> =
        validator.validateValidated(this)
}
