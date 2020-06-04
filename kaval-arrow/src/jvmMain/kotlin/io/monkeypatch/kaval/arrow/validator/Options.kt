package io.monkeypatch.kaval.arrow.validator

import arrow.core.Option
import arrow.core.Some
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.predicate
import io.monkeypatch.kaval.core.whenIsInstance

/**
 * Provide validators for ArrowKt [Option]
 */
object Options {

    /**
     * A validator for [Option] that only accept [arrow.core.None]
     *
     * @param T the option inner type
     * @return the validator
     */
    fun <T> isNone(): Validator<Option<T>> =
        predicate(Option<T>::isEmpty) { "require to be empty" }

    /**
     * A validator for [Option] that only accept [Some]
     *
     * @param T the option inner type
     * @return the validator
     */
    fun <T> isSome(): Validator<Option<T>> =
        predicate(Option<T>::isDefined) { "require to be defined" }

    /**
     * A validator for [Option] that accept [arrow.core.None] or a valid [Some]
     *
     * @param validator provide a T validator for [Some] case
     * @param T the option inner type
     * @return the validator
     */
    fun <T> isNoneOr(validator: () -> Validator<T>): Validator<Option<T>> =
        whenIsInstance {
            field("t", Some<T>::t, validator)
        }
}
