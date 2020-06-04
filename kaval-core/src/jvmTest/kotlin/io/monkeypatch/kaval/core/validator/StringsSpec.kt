package io.monkeypatch.kaval.core.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Strings.hasLength
import io.monkeypatch.kaval.core.validator.Strings.matches
import io.monkeypatch.kaval.core.validator.Strings.maxLength
import io.monkeypatch.kaval.core.validator.Strings.minLength
import io.monkeypatch.kaval.core.validator.Strings.notBlank
import io.monkeypatch.kaval.core.validator.Strings.notEmpty
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class StringsSpec : DescribeSpec() {

    init {

        describe("notEmpty") {
            val validator: Validator<String> = notEmpty

            it("notEmpty should reject an empty string") {
                val result = validator("")
                result should beInvalidWithReason(
                    "requires to be not empty"
                )
            }

            it("notEmpty should accept if instance valid") {
                val result = validator("plop")
                result should beValid()
            }
        }

        describe("notBlank") {
            val validator: Validator<String> = notBlank

            it("notBlank should reject an empty string") {
                val result = validator("")
                result should beInvalidWithReason(
                    "requires to be not blank"
                )
            }

            it("notBlank should reject a blank string") {
                val result = validator("  ")
                result should beInvalidWithReason(
                    "requires to be not blank"
                )
            }

            it("notBlank should accept if instance valid") {
                val result = validator("plop")
                result should beValid()
            }
        }

        describe("hasLength") {
            val validator: Validator<String> = hasLength(4)

            it("hasLength should reject a too short string") {
                val result = validator("foo")
                result should beInvalidWithReason(
                    "requires to be equals to 4, got 3"
                )
            }
            it("hasLength should reject a too long string") {
                val result = validator("foo bar")
                result should beInvalidWithReason(
                    "requires to be equals to 4, got 7"
                )
            }

            it("hasLength should accept if length is valid") {
                val result = validator("plop")
                result should beValid()
            }
        }

        describe("maxLength") {
            val validator: Validator<String> = maxLength(4)

            it("maxLength should accept a too short string") {
                val result = validator("foo")
                result should beValid()
            }
            it("maxLength should reject a too long string") {
                val result = validator("foo bar")
                result should beInvalidWithReason(
                    "requires to be lower or equals to 4, got 7"
                )
            }

            it("maxLength should accept if length is valid") {
                val result = validator("plop")
                result should beValid()
            }
        }

        describe("minLength") {
            val validator: Validator<String> = minLength(4)

            it("minLength should reject a too short string") {
                val result = validator("foo")
                result should beInvalidWithReason(
                    "requires to be greater or equals to 4, got 3"
                )
            }
            it("minLength should accept a too long string") {
                val result = validator("foo bar")
                result should beValid()
            }

            it("minLength should accept if length is valid") {
                val result = validator("plop")
                result should beValid()
            }
        }

        describe("matches") {
            val pattern = "p[a-z]{1,3}p"
            val validator: Validator<String> = matches(Regex(pattern))

            it("matches should reject an invalid string") {
                val result = validator("foo bar")
                result should beInvalidWithReason(
                    "requires matching '$pattern'"
                )
            }

            it("matches should reject an invalid string with explanation") {
                val reason = "plaf"
                val validatorWithReason: Validator<String> = matches(Regex(pattern), reason)
                val result = validatorWithReason("foo bar")
                result should beInvalidWithReason(
                    reason
                )
            }

            it("matches should accept if length is valid") {
                val result = validator("plop")
                result should beValid()
            }
        }
    }
}
