package io.monkeypatch.kaval.coroutine
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.InvalidException
import io.monkeypatch.kaval.core.Valid

class IsValidSpec : DescribeSpec() {

    private val reason = "plaf"
    private val alwaysValid: SuspendValidator<String> = { Valid }
    private val alwaysInvalid: SuspendValidator<String> = { Invalid(reason) }

    init {
        describe("isValid") {
            it("should be true when valid") {
                val result = "plop".isValid(alwaysValid)
                result shouldBe true
            }

            it("should be false when invalid") {
                val result = "plop".isValid(alwaysInvalid)
                result shouldBe false
            }
        }

        describe("isValidOrThrow") {
            it("should be ok when valid") {
                val s = "plop"
                val result = s.isValidOrThrow(alwaysValid)
                result shouldBe s
            }

            it("should throw when invalid") {
                val exception = shouldThrow<InvalidException> {
                    "plop".isValidOrThrow(alwaysInvalid)
                }
                exception.message shouldBe reason
            }
        }
    }
}
