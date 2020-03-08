package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.predicate

/**
 * Validator for [Comparable]
 */
object Comparables {
    /**
     * A validator for _greater than_
     *
     * @param T the element type, should be [Comparable]
     * @param value the value to compare with
     * @return a validator for `>`
     */
    fun <T : Comparable<T>> greaterThan(value: T): Validator<T> =
        predicate({ it > value }) { "require to be greater than $value, got $it" }

    /**
     * A validator for _greater than_
     *
     * @param T the element type
     * @param value the value to compare with
     * @param comparator a comparator for [T]
     * @return a validator for `>`
     */
    fun <T> greaterThan(value: T, comparator: Comparator<T>): Validator<T> =
        predicate({ comparator.compare(it, value) > 0 }) { "require to be greater than $value, got $it" }

    /**
     * A validator for _greater or equals to_
     *
     * @param T the element type, should be [Comparable]
     * @param value the value to compare with
     * @return a validator for `>=`
     */
    fun <T : Comparable<T>> greaterOrEqualTo(value: T): Validator<T> =
        predicate({ it >= value }) { "require to be greater or equals to $value, got $it" }

    /**
     * A validator for _greater or equals to_
     *
     * @param T the element type
     * @param value the value to compare with
     * @param comparator a comparator for [T]
     * @return a validator for `>=`
     */
    fun <T> greaterOrEqualTo(value: T, comparator: Comparator<T>): Validator<T> =
        predicate({ comparator.compare(it, value) >= 0 }) { "require to be greater or equals to $value, got $it" }

    /**
     * A validator for _lower than_
     *
     * @param T the element type, should be [Comparable]
     * @param value the value to compare with
     * @return a validator for `<`
     */
    fun <T : Comparable<T>> lowerThan(value: T): Validator<T> =
        predicate({ it < value }) { "require to be lower than $value, got $it" }

    /**
     * A validator for _lower than_
     *
     * @param T the element type
     * @param value the value to compare with
     * @param comparator a comparator for [T]
     * @return a validator for `<`
     */
    fun <T> lowerThan(value: T, comparator: Comparator<T>): Validator<T> =
        predicate({ comparator.compare(it, value) < 0 }) { "require to be lower than $value, got $it" }

    /**
     * A validator for _lower or equals to_
     *
     * @param T the element type, should be [Comparable]
     * @param value the value to compare with
     * @return a validator for `<=`
     */
    fun <T : Comparable<T>> lowerOrEqualTo(value: T): Validator<T> =
        predicate({ it <= value }) { "require to be lower or equals to $value, got $it" }

    /**
     * A validator for _lower or equals to_
     *
     * @param T the element type
     * @param value the value to compare with
     * @param comparator a comparator for [T]
     * @return a validator for `<=`
     */
    fun <T> lowerOrEqualTo(value: T, comparator: Comparator<T>): Validator<T> =
        predicate({ comparator.compare(it, value) <= 0 }) { "require to be lower or equals to $value, got $it" }

    /**
     * A validator for _equals to_
     *
     * @param T the element type, should be [Comparable]
     * @param value the value to compare with
     * @return a validator for `==`
     */
    fun <T : Comparable<T>> equalsTo(value: T): Validator<T> =
        predicate({ it == value }) { "require to be equals to $value, got $it" }

    /**
     * A validator for _equals to_
     *
     * @param T the element type
     * @param value the value to compare with
     * @param comparator a comparator for [T]
     * @return a validator for `==`
     */
    fun <T> equalsTo(value: T, comparator: Comparator<T>): Validator<T> =
        predicate({ comparator.compare(it, value) == 0 }) { "require to be equals to $value, got $it" }

    /**
     * A validator for _in closed range_ with [Comparable]
     *
     * @param T the element type, should be [Comparable]
     * @param range the range
     * @return a validator for `in`
     */
    fun <T : Comparable<T>> inClosedRange(range: ClosedRange<T>): Validator<T> =
        predicate({ it in range }) { "require to be in range $range, got $it" }
}
