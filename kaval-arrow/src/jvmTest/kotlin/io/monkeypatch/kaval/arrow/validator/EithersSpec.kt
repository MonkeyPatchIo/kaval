package io.monkeypatch.kaval.arrow.validator

import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.validator.Ints
import io.monkeypatch.kaval.core.validator.Strings
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class EithersSpec : DescribeSpec() {
    init {
        describe("Eithers.isLeft()") {
            val validator = Eithers.isLeft<String, Int>()

            it("should be valid when Left") {
                val result = validator("Plop".left())
                result should beValid()
            }

            it("should be invalid when Right") {
                val result = validator(1.right())
                result should beInvalidWithReason("require to be left")
            }
        }

        describe("Eithers.isLeft(validator)") {
            val validator = Eithers.isLeft<String, Int> { Strings.notBlank }

            it("should be valid when Left with a valid value") {
                val result = validator("Plop".left())
                result should beValid()
            }

            it("should be invalid when Left with an invalid value") {
                val result = validator("".left())
                result should beInvalidWithReason("requires to be not blank")
            }

            it("should be invalid when Right") {
                val result = validator(1.right())
                result should beInvalidWithReason("require to be left")
            }
        }

        describe("Eithers.isRight()") {
            val validator = Eithers.isRight<String, Int>()

            it("should be valid when Right") {
                val result = validator(42.right())
                result should beValid()
            }

            it("should be invalid when Left") {
                val result = validator("Plop".left())
                result should beInvalidWithReason("require to be right")
            }
        }

        describe("Eithers.isRight(validator)") {
            val validator = Eithers.isRight<String, Int> { Ints.strictlyPositive }

            it("should be valid when Right with a valid value") {
                val result = validator(42.right())
                result should beValid()
            }

            it("should be invalid when Right with an invalid value") {
                val result = validator(0.right())
                result should beInvalidWithReason("requires to be strictly positive")
            }

            it("should be invalid when Left") {
                val result = validator("Plop".left())
                result should beInvalidWithReason("require to be right")
            }
        }

        describe("Either.validator") {
            val validator = Eithers.validator({ Strings.notBlank }, { Ints.strictlyPositive })

            it("should be valid when Left with a valid value") {
                val result = validator("Plop".left())
                result should beValid()
            }

            it("should be invalid when Left with an invalid value") {
                val result = validator("".left())
                result should beInvalidWithReason("requires to be not blank")
            }

            it("should be valid when Right with a valid value") {
                val result = validator(42.right())
                result should beValid()
            }

            it("should be invalid when Right with an invalid value") {
                val result = validator(0.right())
                result should beInvalidWithReason("requires to be strictly positive")
            }
        }
    }
}
