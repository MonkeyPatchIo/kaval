package io.monkeypatch.kaval.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class IsValidSpec : DescribeSpec() {

    private val reason = "plaf"

    init {
        describe("isValid") {
            it("should be true when valid") {
                val result = "plop".isValid(alwaysValid())
                result shouldBe true
            }

            it("should be false when invalid") {
                val result = "plop".isValid(alwaysInvalid(reason))
                result shouldBe false
            }
        }

        describe("isValidOrThrow") {
            it("should be ok when valid") {
                val s = "plop"
                val result = s.isValidOrThrow(alwaysValid())
                result shouldBe s
            }

            it("should throw when invalid") {
                val exception = shouldThrow<InvalidException> {
                    "plop".isValidOrThrow(alwaysInvalid(reason))
                }
                exception.message shouldBe reason
            }
        }
    }
}
