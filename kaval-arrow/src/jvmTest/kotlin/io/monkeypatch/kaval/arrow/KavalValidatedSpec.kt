package io.monkeypatch.kaval.arrow

import arrow.core.Nel
import io.kotest.assertions.arrow.validation.shouldBeInvalid
import io.kotest.assertions.arrow.validation.shouldBeValid
import io.kotest.core.spec.style.DescribeSpec
import io.monkeypatch.kaval.arrow.KavalValidated.check
import io.monkeypatch.kaval.arrow.KavalValidated.toValidated
import io.monkeypatch.kaval.arrow.KavalValidated.validateValidated
import io.monkeypatch.kaval.core.BaseValidationIssue
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.alwaysInvalid
import io.monkeypatch.kaval.core.alwaysValid

class KavalValidatedSpec : DescribeSpec() {

    private val reason = "plaf"

    init {
        describe("ValidationResult.toValidated") {
            it("should be Valid when valid") {
                val result = Valid.toValidated()
                result shouldBeValid Unit
            }

            it("should be Invalid when invalid") {
                val result = Invalid(reason).toValidated()
                result shouldBeInvalid Nel.of(BaseValidationIssue(reason))
            }
        }

        describe("Validator<T>.validateEither") {
            it("should be Valid when valid") {
                val result = alwaysValid<String>().validateValidated("plop")
                result shouldBeValid "plop"
            }

            it("should be Invalid when invalid") {
                val result = alwaysInvalid<String>(reason).validateValidated("plop")
                result shouldBeInvalid Nel.of(BaseValidationIssue(reason))
            }
        }

        describe("Validated.check") {
            it("should be Valid when valid") {
                val result = "plop".check(alwaysValid())
                result shouldBeValid "plop"
            }

            it("should be Invalid when invalid") {
                val result = "plop".check(alwaysInvalid(reason))
                result shouldBeInvalid Nel.of(BaseValidationIssue(reason))
            }
        }
    }
}
