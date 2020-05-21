package io.monkeypatch.kaval.arrow.validator

import arrow.core.None
import arrow.core.Some
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.validator.Strings
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class OptionsSpec : DescribeSpec() {
    init {
        describe("Options.isNone") {
            val validator = Options.isNone<String>()

            it("should be valid when None") {
                val result = validator.validate(None)
                result should beValid()
            }

            it("should be invalid when Some") {
                val result = validator.validate(Some("Plop"))
                result should beInvalidWithReason("require to be empty")
            }
        }

        describe("Options.isSome") {
            val validator = Options.isSome<String>()

            it("should be valid when Some") {
                val result = validator.validate(Some("Plop"))
                result should beValid()
            }

            it("should be invalid when None") {
                val result = validator.validate(None)
                result should beInvalidWithReason("require to be defined")
            }
        }

        describe("Options.isNoneOr") {
            val validator = Options.isNoneOr { Strings.notBlank }

            it("should be valid when None") {
                val result = validator.validate(None)
                result should beValid()
            }

            it("should be valid when Some with a valid value") {
                val result = validator.validate(Some("Plop"))
                result should beValid()
            }

            it("should be invalid when Some with an invalid value") {
                val result = validator.validate(Some(""))
                result should beInvalidWithReason("requires to be not blank")
            }
        }
    }
}
