package io.monkeypatch.kaval.arrow

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.InvalidException
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.Validator

object KavalEither {

    /**
     * Check an element is valid
     *
     * @param T the element type
     * @return an [Either.Right] with the element if valid, otherwise an [Either.Left] with [InvalidException]
     */
    fun <T> T.check(validator: Validator<T>): Either<InvalidException, T> =
        when (val result = validator.validate(this)) {
            is Valid -> this.right()
            is Invalid -> InvalidException(result).left()
        }
}
