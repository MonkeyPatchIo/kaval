package io.monkeypatch.kaval.arrow.validator

import arrow.core.Nel
import arrow.core.NonEmptyList
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.validator
import io.monkeypatch.kaval.core.validator.Collections
import io.monkeypatch.kaval.core.validator.Comparables

/**
 * Provide validators for ArrowKt [NonEmptyList]
 */
object NonEmptyLists {

    /**
     * A validator check size of a [NonEmptyList]
     *
     * @param T the element type
     * @param size the expected size
     * @return the nel validator
     */
    fun <T> hasSize(size: Int): Validator<Nel<T>> =
        field("size", Nel<T>::size) { Comparables.equalsTo(size) }

    /**
     * A validator check maximum size of a [NonEmptyList]
     *
     * @param T the element type
     * @param size the maximum size
     * @return the nel validator
     */
    fun <T> maxSize(size: Int): Validator<Nel<T>> =
        field("size", Nel<T>::size) { Comparables.lowerOrEqualTo(size) }

    /**
     * A validator check minimum size of a [NonEmptyList]
     *
     * @param T the element type
     * @param size the minimum size
     * @return the nel validator
     */
    fun <T> minSize(size: Int): Validator<Nel<T>> =
        field("size", Nel<T>::size) { Comparables.greaterOrEqualTo(size) }

    /**
     * Validate a [NonEmptyList] if all elements are valid
     * If the [NonEmptyList] is empty, it is valid
     *
     * @param T the element type
     * @param elementValidator the element validator
     * @return the nel validator
     */
    fun <T> allValid(elementValidator: () -> Validator<T>): Validator<Nel<T>> =
        validator { nel ->
            Collections.allValid(elementValidator).validate(nel.asIterable())
        }

    /**
     * Validate a [NonEmptyList] that requires at least one element valid
     * If the [NonEmptyList] is empty, it is not valid
     *
     * @param T the element type
     * @param elementValidator the element validator
     * @return the nel validator
     */
    fun <T> atLeastOneValid(elementValidator: () -> Validator<T>): Validator<Nel<T>> =
        validator { nel ->
            Collections.atLeastOneValid(elementValidator).validate(nel.asIterable())
        }

    private fun <T> Nel<T>.asIterable(): Iterable<T> {
        val that = this
        return object : Iterable<T> {
            override fun iterator(): Iterator<T> =
                that.iterator()
        }
    }
}
