package io.monkeypatch.kaval.arrow

import io.kotest.assertions.arrow.either.beLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.arrow.KavalEither.check
import io.monkeypatch.kaval.core.alwaysInvalid
import io.monkeypatch.kaval.core.alwaysValid

class KavalEitherSpec : DescribeSpec() {

    init {
        describe("Either.check") {
            it("should be true when valid") {
                val result = "plop".check(alwaysValid())
                result shouldBeRight "plop"
            }

            it("should be false when invalid") {
                val result = "plop".check(alwaysInvalid("plaf"))
                result should beLeft()
            }
        }
    }
}
