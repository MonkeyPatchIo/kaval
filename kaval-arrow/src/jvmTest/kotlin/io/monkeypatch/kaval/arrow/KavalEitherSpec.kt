package io.monkeypatch.kaval.arrow

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.monkeypatch.kaval.arrow.KavalEither.check
import io.monkeypatch.kaval.arrow.KavalEither.toEither
import io.monkeypatch.kaval.arrow.KavalEither.validateEither
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.InvalidException
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.alwaysInvalid
import io.monkeypatch.kaval.core.alwaysValid

class KavalEitherSpec : DescribeSpec() {

    private val reason = "plaf"

    init {
        describe("ValidationResult.toEither") {
            it("should be Right when valid") {
                val result = Valid.toEither()
                result shouldBeRight Unit
            }

            it("should be Left when invalid") {
                val result = Invalid(reason).toEither()
                result shouldBeLeft InvalidException(Invalid(reason))
            }
        }

        describe("Validator<T>.validateEither") {
            it("should be Right when valid") {
                val result = alwaysValid<String>().validateEither("plop")
                result shouldBeRight "plop"
            }

            it("should be Left when invalid") {
                val result = alwaysInvalid<String>(reason).validateEither("plop")
                result shouldBeLeft InvalidException(Invalid(reason))
            }
        }

        describe("check with Either") {
            it("should be Right when valid") {
                val result = "plop".check(alwaysValid())
                result shouldBeRight "plop"
            }

            it("should be Left when invalid") {
                val result = "plop".check(alwaysInvalid(reason))
                result shouldBeLeft InvalidException(Invalid(reason))
            }
        }
    }
}
