package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.predicate
import io.monkeypatch.kaval.core.validator.Comparables.equalsTo
import io.monkeypatch.kaval.core.validator.Comparables.greaterOrEqualTo
import io.monkeypatch.kaval.core.validator.Comparables.lowerOrEqualTo

/**
 * Validators for [CharSequence] or [String]
 */
object Strings {

    /**
     * A validator to reject empty [String] (or [CharSequence])
     */
    val notEmpty: Validator<CharSequence> =
        predicate(CharSequence::isNotEmpty) { "require to be not empty" }

    /**
     * A validator to reject blank [String] (or [CharSequence])
     */
    val notBlank: Validator<CharSequence> =
        predicate(CharSequence::isNotBlank) { "require to be not blank" }

    /**
     * A validator check length of a [String] (or [CharSequence])
     *
     * @param length the expected length
     */
    fun hasLength(length: Int): Validator<CharSequence> =
        field("length", CharSequence::length) { equalsTo(length) }

    /**
     * A validator check maximum length of a [String] (or [CharSequence])
     *
     * @param length the maximum length
     */
    fun maxLength(length: Int): Validator<CharSequence> =
        field("length", CharSequence::length) { lowerOrEqualTo(length) }

    /**
     * A validator check minimum length of a [String] (or [CharSequence])
     *
     * @param length the minimum length
     */
    fun minLength(length: Int): Validator<CharSequence> =
        field("length", CharSequence::length) { greaterOrEqualTo(length) }

    /**
     * A validator check matching a [Regex] for a [String] (or [CharSequence])
     *
     * @param regex the [Regex]
     * @param message (optional) an explanation
     */
    fun matches(regex: Regex, message: String = "require matching '${regex.pattern}'"): Validator<CharSequence> =
        predicate(regex::containsMatchIn) { message }
}
