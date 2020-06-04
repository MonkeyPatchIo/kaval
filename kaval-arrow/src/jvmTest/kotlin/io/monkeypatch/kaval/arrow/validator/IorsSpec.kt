package io.monkeypatch.kaval.arrow.validator

import arrow.core.bothIor
import arrow.core.leftIor
import arrow.core.rightIor
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.validator.Ints
import io.monkeypatch.kaval.core.validator.Strings
import io.monkeypatch.kaval.kotlintest.beInvalidWithAllReasons
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class IorsSpec : DescribeSpec() {
    init {
        describe("Iors.isLeft()") {
            val validator = Iors.isLeft<String, Int>()

            it("should be valid when Left") {
                val result = validator("Plop".leftIor())
                result should beValid()
            }

            it("should be invalid when Right") {
                val result = validator(1.rightIor())
                result should beInvalidWithReason("require to be left")
            }

            it("should be invalid when Both") {
                val result = validator(("Plop" to 1).bothIor())
                result should beInvalidWithReason("require to be left")
            }
        }

        describe("Iors.isLeft(validator)") {
            val validator = Iors.isLeft<String, Int> { Strings.notBlank }

            it("should be valid when Left with a valid value") {
                val result = validator("Plop".leftIor())
                result should beValid()
            }

            it("should be invalid when Left with an invalid value") {
                val result = validator("".leftIor())
                result should beInvalidWithReason("requires to be not blank")
            }

            it("should be invalid when Right") {
                val result = validator(1.rightIor())
                result should beInvalidWithReason("require to be left")
            }

            it("should be invalid when Both") {
                val result = validator(("Plop" to 1).bothIor())
                result should beInvalidWithReason("require to be left")
            }
        }

        describe("Iors.isRight()") {
            val validator = Iors.isRight<String, Int>()

            it("should be valid when Right") {
                val result = validator(42.rightIor())
                result should beValid()
            }

            it("should be invalid when Left") {
                val result = validator("Plop".leftIor())
                result should beInvalidWithReason("require to be right")
            }

            it("should be invalid when Both") {
                val result = validator(("Plop" to 1).bothIor())
                result should beInvalidWithReason("require to be right")
            }
        }

        describe("Iors.isRight(validator)") {
            val validator = Iors.isRight<String, Int> { Ints.strictlyPositive }

            it("should be valid when Right with a valid value") {
                val result = validator(42.rightIor())
                result should beValid()
            }

            it("should be invalid when Right with an invalid value") {
                val result = validator(0.rightIor())
                result should beInvalidWithReason("requires to be strictly positive")
            }

            it("should be invalid when Left") {
                val result = validator("Plop".leftIor())
                result should beInvalidWithReason("require to be right")
            }

            it("should be invalid when Both") {
                val result = validator(("Plop" to 1).bothIor())
                result should beInvalidWithReason("require to be right")
            }
        }

        describe("Iors.isBoth()") {
            val validator = Iors.isBoth<String, Int>()

            it("should be valid when Both") {
                val result = validator(("Plop" to 1).bothIor())
                result should beValid()
            }

            it("should be invalid when Left") {
                val result = validator("Plop".leftIor())
                result should beInvalidWithReason("require to be both")
            }

            it("should be invalid when Right") {
                val result = validator(42.rightIor())
                result should beInvalidWithReason("require to be both")
            }
        }

        describe("Iors.isBoth(lv, rv)") {
            val validator = Iors.isBoth({ Strings.notBlank }, { Ints.strictlyPositive })

            it("should be valid when Both with a valid values") {
                val result = validator(("Plop" to 42).bothIor())
                result should beValid()
            }

            it("should be invalid when Both with a left invalid value") {
                val result = validator(("" to 42).bothIor())
                result should beInvalidWithReason("requires to be not blank")
            }

            it("should be invalid when Both with a right invalid value") {
                val result = validator(("plop" to 0).bothIor())
                result should beInvalidWithReason("requires to be strictly positive")
            }

            it("should be invalid when Both with a both invalid") {
                val result = validator(("" to 0).bothIor())
                result should beInvalidWithAllReasons(
                    "requires to be not blank",
                    "requires to be strictly positive"
                )
            }

            it("should be invalid when Left") {
                val result = validator("Plop".leftIor())
                result should beInvalidWithReason("require to be both")
            }

            it("should be invalid when Right") {
                val result = validator(1.rightIor())
                result should beInvalidWithReason("require to be both")
            }
        }

        describe("Ior.validator") {
            val validator = Iors.validator({ Strings.notBlank }, { Ints.strictlyPositive })

            it("should be valid when Left with a valid value") {
                val result = validator("Plop".leftIor())
                result should beValid()
            }

            it("should be invalid when Left with an invalid value") {
                val result = validator("".leftIor())
                result should beInvalidWithReason("requires to be not blank")
            }

            it("should be valid when Right with a valid value") {
                val result = validator(42.rightIor())
                result should beValid()
            }

            it("should be invalid when Right with an invalid value") {
                val result = validator(0.rightIor())
                result should beInvalidWithReason("requires to be strictly positive")
            }

            it("should be valid when Both with a valid values") {
                val result = validator(("Plop" to 42).bothIor())
                result should beValid()
            }

            it("should be invalid when Both with a left invalid value") {
                val result = validator(("" to 42).bothIor())
                result should beInvalidWithReason("requires to be not blank")
            }

            it("should be invalid when Both with a right invalid value") {
                val result = validator(("plop" to 0).bothIor())
                result should beInvalidWithReason("requires to be strictly positive")
            }

            it("should be invalid when Both with a both invalid") {
                val result = validator(("" to 0).bothIor())
                result should beInvalidWithAllReasons(
                    "requires to be not blank",
                    "requires to be strictly positive"
                )
            }
        }
    }
}
