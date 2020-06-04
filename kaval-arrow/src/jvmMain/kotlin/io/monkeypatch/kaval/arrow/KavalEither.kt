package io.monkeypatch.kaval.arrow

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.InvalidException
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationResult
import io.monkeypatch.kaval.core.Validator

object KavalEither {

    /**
     * A [ValidationResult] is isomorphic to [Either]
     *
     * @return an `Either<InvalidException, Unit>`
     */
    fun ValidationResult.toEither(): Either<InvalidException, Unit> =
        when (this) {
            is Valid -> Unit.right()
            is Invalid -> InvalidException(this).left()
        }

    /**
     * Validate and produce an [Either]
     *
     * @param t the element
     * @param T the element type
     * @return an `Either<InvalidException, T>`
     */
    fun <T> Validator<T>.validateEither(t: T): Either<InvalidException, T> =
        this(t)
            .toEither()
            .map { t }

    /**
     * Check an element is valid
     *
     * @param T the element type
     * @return an [Either.Right] with the element if valid, otherwise an [Either.Left] with [InvalidException]
     */
    fun <T> T.check(validator: Validator<T>): Either<InvalidException, T> =
        validator.validateEither(this)
}
