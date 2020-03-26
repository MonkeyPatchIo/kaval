package io.monkeypatch.kaval.kotlintest

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.monkeypatch.kaval.core.alwaysInvalid
import io.monkeypatch.kaval.core.alwaysValid

class MatchersSpec : DescribeSpec() {

    init {
        describe("beValidFor") {

            it("should return a success when valid") {
                val matcher = beValidFor(alwaysValid<String>())
                val result = matcher.test(" ")
                result.passed() shouldBe true
            }

            it("should return a failure when invalid") {
                val reason = "Plaf"
                val matcher = beValidFor(alwaysInvalid<String>(reason))
                val result = matcher.test("plop")
                result.passed() shouldBe false
                result.failureMessage() shouldBe "Should be valid, got Invalid: $reason"
                result.negatedFailureMessage() shouldBe "Should be invalid, got plop"
            }
        }
    }
}
