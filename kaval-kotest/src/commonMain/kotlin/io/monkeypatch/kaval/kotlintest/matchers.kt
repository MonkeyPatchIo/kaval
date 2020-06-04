package io.monkeypatch.kaval.kotlintest

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.Validator

/**
 * Check an element is valid
 *
 * @param validator the validator
 * @return the [Matcher]
 */
fun <T> beValidFor(validator: Validator<T>): Matcher<T> = object : Matcher<T> {
    override fun test(value: T): MatcherResult {
        val result = validator(value)
        return MatcherResult.Companion(
            result is Valid,
            "Should be valid, got $result",
            "Should be invalid, got $value"
        )
    }
}
