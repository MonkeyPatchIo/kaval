package io.monkeypatch.kaval.core.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Comparables.equalsTo
import io.monkeypatch.kaval.core.validator.Comparables.greaterOrEqualTo
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan
import io.monkeypatch.kaval.core.validator.Comparables.inClosedRange
import io.monkeypatch.kaval.core.validator.Comparables.lowerOrEqualTo
import io.monkeypatch.kaval.core.validator.Comparables.lowerThan
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class ComparableSpec : DescribeSpec() {

    init {
        describe("greaterThan") {
            val validator: Validator<Int> = greaterThan(4)

            it("greaterThan should accept a greater value") {
                val result = validator.validate(42)
                result should beValid()
            }
            it("greaterThan should reject same value") {
                val result = validator.validate(4)
                result should beInvalidWithReason(
                    "requires to be greater than 4, got 4"
                )
            }

            it("greaterThan should reject a lower value") {
                val result = validator.validate(2)
                result should beInvalidWithReason(
                    "requires to be greater than 4, got 2"
                )
            }
        }

        describe("greaterThan with Comparator") {
            val validator: Validator<Int> = greaterThan(4, naturalOrder())

            it("greaterThan should accept a greater value") {
                val result = validator.validate(42)
                result should beValid()
            }
            it("greaterThan should reject same value") {
                val result = validator.validate(4)
                result should beInvalidWithReason(
                    "requires to be greater than 4, got 4"
                )
            }

            it("greaterThan should reject a lower value") {
                val result = validator.validate(2)
                result should beInvalidWithReason(
                    "requires to be greater than 4, got 2"
                )
            }
        }

        describe("greaterOrEqualTo") {
            val validator: Validator<Int> = greaterOrEqualTo(4)

            it("greaterOrEqualTo should accept a greater value") {
                val result = validator.validate(42)
                result should beValid()
            }
            it("greaterOrEqualTo should accept same value") {
                val result = validator.validate(4)
                result should beValid()
            }

            it("greaterOrEqualTo should reject a lower value") {
                val result = validator.validate(2)
                result should beInvalidWithReason(
                    "requires to be greater or equals to 4, got 2"
                )
            }
        }

        describe("greaterOrEqualTo with Comparator") {
            val validator: Validator<Int> = greaterOrEqualTo(4, naturalOrder())

            it("greaterOrEqualTo should accept a greater value") {
                val result = validator.validate(42)
                result should beValid()
            }
            it("greaterOrEqualTo should accept same value") {
                val result = validator.validate(4)
                result should beValid()
            }

            it("greaterOrEqualTo should reject a lower value") {
                val result = validator.validate(2)
                result should beInvalidWithReason(
                    "requires to be greater or equals to 4, got 2"
                )
            }
        }

        describe("lowerThan") {
            val validator: Validator<Int> = lowerThan(4)

            it("lowerThan should accept a greater value") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be lower than 4, got 42"
                )
            }
            it("lowerThan should reject same value") {
                val result = validator.validate(4)
                result should beInvalidWithReason(
                    "requires to be lower than 4, got 4"
                )
            }

            it("lowerThan should reject a lower value") {
                val result = validator.validate(2)
                result should beValid()
            }
        }

        describe("lowerThan with Comparator") {
            val validator: Validator<Int> = lowerThan(4, naturalOrder())

            it("lowerThan should accept a greater value") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be lower than 4, got 42"
                )
            }
            it("lowerThan should reject same value") {
                val result = validator.validate(4)
                result should beInvalidWithReason(
                    "requires to be lower than 4, got 4"
                )
            }

            it("lowerThan should reject a lower value") {
                val result = validator.validate(2)
                result should beValid()
            }
        }

        describe("lowerOrEqualTo") {
            val validator: Validator<Int> = lowerOrEqualTo(4)

            it("lowerOrEqualTo should accept a greater value") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be lower or equals to 4, got 42"
                )
            }
            it("lowerOrEqualTo should accept same value") {
                val result = validator.validate(4)
                result should beValid()
            }

            it("lowerOrEqualTo should reject a lower value") {
                val result = validator.validate(2)
                result should beValid()
            }
        }

        describe("lowerOrEqualTo with Comparator") {
            val validator: Validator<Int> = lowerOrEqualTo(4, naturalOrder())

            it("lowerOrEqualTo should accept a greater value") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be lower or equals to 4, got 42"
                )
            }
            it("lowerOrEqualTo should accept same value") {
                val result = validator.validate(4)
                result should beValid()
            }

            it("lowerOrEqualTo should reject a lower value") {
                val result = validator.validate(2)
                result should beValid()
            }
        }

        describe("equalsTo") {
            val validator: Validator<Int> = equalsTo(4)

            it("equalsTo should reject a wrong number") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be equals to 4, got 42"
                )
            }
            it("equalsTo should accept value is Ok") {
                val result = validator.validate(4)
                result should beValid()
            }
        }

        describe("equalsTo with Comparator") {
            val validator: Validator<Int> = equalsTo(4, naturalOrder())

            it("equalsTo should reject a wrong number") {
                val result = validator.validate(42)
                result should beInvalidWithReason(
                    "requires to be equals to 4, got 42"
                )
            }
            it("equalsTo should accept value is Ok") {
                val result = validator.validate(4)
                result should beValid()
            }
        }

        describe("inClosedRange") {
            val validator: Validator<String> = inClosedRange("a".."f")

            it("inClosedRange should reject a lower string") {
                val result = validator.validate("0")
                result should beInvalidWithReason(
                    "requires to be in range a..f, got 0"
                )
            }
            it("inClosedRange should reject a higher string") {
                val result = validator.validate("z")
                result should beInvalidWithReason(
                    "requires to be in range a..f, got z"
                )
            }
            it("inClosedRange should accept value lower bound") {
                val result = validator.validate("a")
                result should beValid()
            }
            it("inClosedRange should accept in range") {
                val result = validator.validate("d")
                result should beValid()
            }
            it("inClosedRange should accept value upper bound") {
                val result = validator.validate("f")
                result should beValid()
            }
        }
    }
}
