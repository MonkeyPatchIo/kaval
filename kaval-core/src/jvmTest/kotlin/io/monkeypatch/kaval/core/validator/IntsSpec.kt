package io.monkeypatch.kaval.core.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.validator.Ints.inRange
import io.monkeypatch.kaval.core.validator.Ints.negative
import io.monkeypatch.kaval.core.validator.Ints.positive
import io.monkeypatch.kaval.core.validator.Ints.strictlyNegative
import io.monkeypatch.kaval.core.validator.Ints.strictlyPositive
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class IntsSpec : DescribeSpec() {

    init {

        describe("inRange") {
            val validator = inRange(5..10)

            it("inRange should reject a lower number") {
                val result = validator.validate(0)
                result should beInvalidWithReason(
                    "requires to be in range 5..10, got 0"
                )
            }
            it("inRange should reject a higher number") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be in range 5..10, got 42"
                )
            }
            it("inRange should accept value lower bound") {
                val result = validator.validate(5)
                result should beValid()
            }
            it("inRange should accept in range") {
                val result = validator.validate(10)
                result should beValid()
            }
            it("inRange should accept value upper bound") {
                val result = validator.validate(7)
                result should beValid()
            }
        }

        describe("strictlyPositive") {
            val validator = strictlyPositive

            it("strictlyPositive should reject a negative number") {
                val result = validator.validate(-1)
                result should beInvalidWithReason(
                    "requires to be strictly positive"
                )
            }
            it("strictlyPositive should reject zero") {
                val result = validator.validate(0)
                result should beInvalidWithReason(
                    "requires to be strictly positive"
                )
            }
            it("strictlyPositive should accept a positive number") {
                val result = validator.validate(5)
                result should beValid()
            }
        }

        describe("positive") {
            val validator = positive

            it("positive should reject a negative number") {
                val result = validator.validate(-1)
                result should beInvalidWithReason(
                    "requires to be positive"
                )
            }
            it("positive should reject zero") {
                val result = validator.validate(0)
                result should beValid()
            }
            it("positive should accept a positive number") {
                val result = validator.validate(5)
                result should beValid()
            }
        }

        describe("strictlyNegative") {
            val validator = strictlyNegative

            it("strictlyNegative should reject a positive number") {
                val result = validator.validate(1)
                result should beInvalidWithReason(
                    "requires to be strictly negative"
                )
            }
            it("strictlyNegative should reject zero") {
                val result = validator.validate(0)
                result should beInvalidWithReason(
                    "requires to be strictly negative"
                )
            }
            it("strictlyNegative should accept a negative number") {
                val result = validator.validate(-5)
                result should beValid()
            }
        }

        describe("negative") {
            val validator = negative

            it("negative should reject a postive number") {
                val result = validator.validate(1)
                result should beInvalidWithReason(
                    "requires to be negative"
                )
            }
            it("negative should reject zero") {
                val result = validator.validate(0)
                result should beValid()
            }
            it("negative should accept a negative number") {
                val result = validator.validate(-5)
                result should beValid()
            }
        }
    }
}
