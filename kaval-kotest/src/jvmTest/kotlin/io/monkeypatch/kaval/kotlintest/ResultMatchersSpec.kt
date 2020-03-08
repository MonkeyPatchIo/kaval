package io.monkeypatch.kaval.kotlintest

import io.kotlintest.matchers.startWith
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid

class ResultMatchersSpec : DescribeSpec() {
    private val reason = "plaf"
    private val invalid = Invalid(reason)
    private val field = "field"
    private val invalidField = Invalid(field, reason)

    init {
        describe("beValid") {
            val matcher = beValid()

            it("should return a success when valid") {
                val result = matcher.test(Valid)
                result.passed() shouldBe true
            }
            it("should return a failure when invalid") {
                val result = matcher.test(invalid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Invalid: $reason should be Valid"
                result.negatedFailureMessage() shouldBe "Invalid: $reason should not be Valid"
            }
        }

        describe("beInvalid") {
            val matcher = beInvalid()

            it("should return a success when invalid") {
                val result = matcher.test(invalid)
                result.passed() shouldBe true
            }
            it("should return a failure when invalid") {
                val result = matcher.test(Valid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Valid should be Invalid"
                result.negatedFailureMessage() shouldBe "Valid should not be Invalid"
            }
        }

        describe("beInvalidOnField") {
            val matcher = beInvalidOnField(field)

            it("should return a success when invalid") {
                val result = matcher.test(invalidField)
                result.passed() shouldBe true
            }
            it("should return a failure when valid") {
                val result = matcher.test(Valid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Valid should be Invalid on field $field"
                result.negatedFailureMessage() shouldBe "Valid should not be Invalid on field $field"
            }
            it("should return a failure when invalid without field") {
                val result = matcher.test(invalid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Invalid: $reason should be Invalid on field $field"
                result.negatedFailureMessage() shouldBe "Invalid: $reason should not be Invalid on field $field"
            }
            it("should return a failure when invalid with another field") {
                val result = matcher.test(Invalid("_$field", reason))
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Invalid: [_$field] $reason should be Invalid on field $field"
                result.negatedFailureMessage() shouldBe "Invalid: [_$field] $reason should not be Invalid on field $field"
            }
        }

        describe("beInvalidWithReason") {
            val matcher = beInvalidWithReason(reason)

            it("should return a success when invalid with reason") {
                val result = matcher.test(invalidField)
                result.passed() shouldBe true
            }
            it("should return a failure when valid") {
                val result = matcher.test(Valid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Valid should be Invalid with reason $reason"
                result.negatedFailureMessage() shouldBe "Valid should not be Invalid with reason $reason"
            }
            it("should return a failure when invalid without reason") {
                val result = matcher.test(Invalid("_$reason"))
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Invalid: _$reason should be Invalid with reason $reason"
                result.negatedFailureMessage() shouldBe "Invalid: _$reason should not be Invalid with reason $reason"
            }
        }

        describe("beInvalidWithAllReasons") {
            val reason2 = "plouf"
            val invalid2 = Invalid(reason2)
            val matcher = beInvalidWithAllReasons(reason, reason2)

            it("should return a success when invalid with reasons") {
                val result = matcher.test(invalid concat invalid2)
                result.passed() shouldBe true
            }
            it("should return a success when invalid with reasons and field") {
                val result = matcher.test(invalidField concat invalid2)
                result.passed() shouldBe true
            }

            it("should return a failure when valid") {
                val result = matcher.test(Valid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Valid should be Invalid with reasons $reason, $reason2"
                result.negatedFailureMessage() shouldBe "Valid should not be Invalid with reasons $reason, $reason2"
            }
            it("should return a failure when invalid only one reason") {
                val result = matcher.test(invalid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "$invalid should be Invalid with reasons $reason, $reason2"
                result.negatedFailureMessage() shouldBe "$invalid should not be Invalid with reasons $reason, $reason2"
            }
            it("should return a failure when invalid only no reason") {
                val otherInvalid = Invalid("_$reason")
                val result = matcher.test(otherInvalid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "$otherInvalid should be Invalid with reasons $reason, $reason2"
                result.negatedFailureMessage() shouldBe "$otherInvalid should not be Invalid with reasons $reason, $reason2"
            }
        }

        describe("beInvalidWithAny") {
            val matcher = beInvalidWithAny(startWith("pl"))

            it("should return a success when invalid with reason") {
                val result = matcher.test(invalidField)
                result.passed() shouldBe true
            }
            it("should return a failure when valid") {
                val result = matcher.test(Valid)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Valid should be Invalid with any reason"
                result.negatedFailureMessage() shouldBe "Valid should not be Invalid with any reason"
            }
            it("should return a failure when invalid with another reason") {
                val other = Invalid("_$reason")
                val result = matcher.test(other)
                result.passed() shouldBe false
                result.failureMessage() shouldBe "$other should be Invalid with any reason, _plaf should start with pl (diverged at index 0)"
                result.negatedFailureMessage() shouldBe "$other should not be Invalid with any reason, _plaf should start with pl (diverged at index 0)"
            }
        }
    }
}
