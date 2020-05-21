package io.monkeypatch.kaval.kotlintest

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.monkeypatch.kaval.core.FieldValidationIssue
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationResult

/**
 * Check a [ValidationResult] is [Valid]
 *
 * @return the [Matcher]
 */
fun beValid() = object : Matcher<ValidationResult> {
    override fun test(value: ValidationResult): MatcherResult =
        MatcherResult(
            value is Valid,
            "$value should be Valid",
            "$value should not be Valid"
        )
}

/**
 * Check a [ValidationResult] is [Invalid]
 *
 * @return the [Matcher]
 */
fun beInvalid() = object : Matcher<ValidationResult> {
    override fun test(value: ValidationResult): MatcherResult =
        MatcherResult(
            value is Invalid,
            "$value should be Invalid",
            "$value should not be Invalid"
        )
}

/**
 * Check a [ValidationResult] is [Invalid] on a field
 *
 * @param field the field name
 * @return the [Matcher]
 */
fun beInvalidOnField(field: String) = object : Matcher<ValidationResult> {
    override fun test(value: ValidationResult): MatcherResult =
        MatcherResult(
            value is Invalid &&
                value.reasons.any { it is FieldValidationIssue && it.field == field },
            "$value should be Invalid on field $field",
            "$value should not be Invalid on field $field"
        )
}

/**
 * Check a [ValidationResult] is [Invalid] with a specific reason
 *
 * @param reason the reason
 * @return the [Matcher]
 */
fun beInvalidWithReason(reason: String) = object : Matcher<ValidationResult> {
    override fun test(value: ValidationResult): MatcherResult =
        MatcherResult(
            value is Invalid &&
                value.reasons.any { it.message == reason },
            "$value should be Invalid with reason '$reason'",
            "$value should not be Invalid with reason '$reason'"
        )
}

/**
 * Check a [ValidationResult] is [Invalid] with all reasons
 *
 * @param firstReason the first reasons
 * @param reasons other reasons
 * @return the [Matcher]
 */
fun beInvalidWithAllReasons(firstReason: String, vararg reasons: String) = object : Matcher<ValidationResult> {
    override fun test(value: ValidationResult): MatcherResult {
        val allReasons = listOf(firstReason) + reasons
        return MatcherResult(
            value is Invalid &&
                allReasons.all { reason ->
                    value.reasons.any { it.message == reason }
                },
            "$value should be Invalid with reasons ${allReasons.joinToString()}",
            "$value should not be Invalid with reasons ${allReasons.joinToString()}"
        )
    }
}

/**
 * Check a [ValidationResult] is [Invalid] with a specific reason [Matcher]
 *
 * @param reasonMatcher the reason [Matcher]
 * @return the [Matcher]
 */
fun beInvalidWithAny(reasonMatcher: Matcher<String?>) = object : Matcher<ValidationResult> {
    override fun test(value: ValidationResult): MatcherResult {
        val results = when (value) {
            is Invalid -> value.reasons.map { reasonMatcher.test(it.message) }
            is Valid -> emptyList()
        }

        val message = if (results.isEmpty()) ""
        else results.joinToString(prefix = ", ") { it.failureMessage() }
        return MatcherResult(
            value is Invalid && results.any { it.passed() },
            "$value should be Invalid with any reason$message",
            "$value should not be Invalid with any reason$message"
        )
    }
}
