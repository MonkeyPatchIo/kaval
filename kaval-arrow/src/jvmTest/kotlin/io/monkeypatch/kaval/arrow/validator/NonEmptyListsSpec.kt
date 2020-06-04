package io.monkeypatch.kaval.arrow.validator

import arrow.core.NonEmptyList
import arrow.core.NonEmptyList.Companion.of
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith
import io.monkeypatch.kaval.arrow.validator.NonEmptyLists.allValid
import io.monkeypatch.kaval.arrow.validator.NonEmptyLists.atLeastOneValid
import io.monkeypatch.kaval.arrow.validator.NonEmptyLists.hasSize
import io.monkeypatch.kaval.arrow.validator.NonEmptyLists.minSize
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan
import io.monkeypatch.kaval.kotlintest.beInvalidWithAllReasons
import io.monkeypatch.kaval.kotlintest.beInvalidWithAny
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class NonEmptyListsSpec : DescribeSpec() {
    init {

        describe("hasSize") {
            val validator: Validator<NonEmptyList<Int>> = hasSize(3)

            it("hasSize should reject a list with invalid size") {
                val result = validator(of(1))
                result should beInvalidWithReason("requires to be equals to 3, got 1")
            }
            it("hasSize should accept a well-sized list") {
                val result = validator(of(1, 2, 3))
                result should beValid()
            }
        }

        describe("maxSize") {
            val validator: Validator<NonEmptyList<Int>> = NonEmptyLists.maxSize(3)

            it("maxSize should accept a smaller list") {
                val result = validator(of(1))
                result should beValid()
            }
            it("maxSize should accept a well-sized list") {
                val result = validator(of(1, 2, 3))
                result should beValid()
            }
            it("maxSize should reject a bigger list") {
                val result = validator(of(1, 2, 3, 4, 5))
                result should beInvalidWithReason("requires to be lower or equals to 3, got 5")
            }
        }

        describe("minSize") {
            val validator: Validator<NonEmptyList<Int>> = minSize(3)

            it("minSize should reject a smaller list") {
                val result = validator(of(1))
                result should beInvalidWithReason("requires to be greater or equals to 3, got 1")
            }
            it("minSize should accept a well-sized list") {
                val result = validator(of(1, 2, 3))
                result should beValid()
            }
            it("minSize should accept a bigger list") {
                val result = validator(of(1, 2, 3, 4, 5))
                result should beValid()
            }
        }

        describe("allValid") {
            val validator: Validator<NonEmptyList<Int>> = allValid { greaterThan(9) }

            it("allValid should accept a list with all element valid") {
                val result = validator(of(10, 11, 12))
                result should beValid()
            }

            it("allValid should reject a list with at least an element invalid") {
                val result = validator(of(10, 4, 12))
                result should beInvalidWithReason("requires to be greater than 9, got 4")
            }

            it("allValid should reject a list with all elements invalid") {
                val result = validator(of(1, 2, 3))
                result should beInvalidWithAllReasons(
                    "requires to be greater than 9, got 1",
                    "requires to be greater than 9, got 2",
                    "requires to be greater than 9, got 3"
                )
            }
        }

        describe("atLeastOneValid") {
            val validator: Validator<NonEmptyList<Int>> = atLeastOneValid { greaterThan(9) }

            it("atLeastOneValid should accept a list with all element valid") {
                val result = validator(of(10, 11, 12))
                result should beValid()
            }

            it("atLeastOneValid should accept a list with at least an element invalid") {
                val result = validator(of(10, 4, 12))
                result should beValid()
            }

            it("atLeastOneValid should reject a list with all elements invalid") {
                val result = validator(of(1, 2, 3))
                result should beInvalidWithAny(
                    startWith("requires at least one element valid, all elements are invalid")
                )
            }
        }
    }
}
