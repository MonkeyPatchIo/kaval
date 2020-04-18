package io.monkeypatch.kaval.arrow

import arrow.core.Nel
import arrow.core.Validated
import arrow.core.invalid
import arrow.core.nel
import arrow.core.valid
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationIssue
import io.monkeypatch.kaval.core.Validator

object KavalValidated {

    /**
     * Check an element is valid
     *
     * @param T the element type
     * @return an [Validated.Valid] with the element if valid, otherwise an [Validated.Invalid] with a not empty list of [ValidationIssue]
     */
    fun <T> T.check(validator: Validator<T>): Validated<Nel<ValidationIssue>, T> =
        when (val result = validator.validate(this)) {
            is Valid -> this.valid()
            is Invalid -> {
                val head = result.issues.first()
                val tail = result.issues.drop(1)
                (head.nel() + tail).invalid()
            }
        }
}
