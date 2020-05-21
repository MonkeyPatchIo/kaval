package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.validator

/**
 * Validators for [Pair] and [Triple]
 */
object Tuples {

    /**
     * Build a pair validator
     *
     * @param tValidator a validator for the first pair argument
     * @param uValidator a validator for the second pair argument
     * @param T the first pair type
     * @param U the second pair type
     * @return a `Validator<Pair<T, U>>`
     */
    fun <T, U> pair(tValidator: Validator<T>, uValidator: Validator<U>): Validator<Pair<T, U>> =
        field("first", Pair<T, U>::first) { tValidator } and
            field("second", Pair<T, U>::second) { uValidator }

    /**
     * Build a triple validator
     *
     * @param tValidator a validator for the first triple argument
     * @param uValidator a validator for the second triple argument
     * @param wValidator a validator for the thrid triple argument
     * @param T the first triple type
     * @param U the second triple type
     * @param W the thrid triple type
     * @return a `Validator<Triple<T, U, W>>`
     */
    fun <T, U, W> triple(
        tValidator: Validator<T>,
        uValidator: Validator<U>,
        wValidator: Validator<W>
    ): Validator<Triple<T, U, W>> =
        field("first", Triple<T, U, W>::first) { tValidator } and
            field("second", Triple<T, U, W>::second) { uValidator } and
            field("third", Triple<T, U, W>::third) { wValidator }
}
