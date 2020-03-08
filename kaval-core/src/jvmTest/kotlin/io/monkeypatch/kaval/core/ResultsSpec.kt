package io.monkeypatch.kaval.core

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table

class ResultsSpec : DescribeSpec() {

    val valid: ValidationResult = Valid
    val invalid: ValidationResult = Invalid("plaf!")
    val invalidField: ValidationResult = Invalid("length", "plouf!")

    val mapper: (ValidationIssue) -> ValidationIssue = { elt ->
        when (elt) {
            is BaseValidationIssue -> BaseValidationIssue("UPDATE: ${elt.message}")
            is FieldValidationIssue -> FieldValidationIssue(elt.field, "UPDATE: ${elt.message}")
        }
    }

    init {
        describe("mapReason") {

            it("valid result should be mapped as valid") {
                val result = valid.mapReason(mapper)

                result shouldBe valid
            }

            it("invalid BaseElementReason should be mapped") {
                val result = invalid.mapReason(mapper)

                result shouldBe Invalid("UPDATE: plaf!")
            }

            it("invalid ElementFieldReason should be mapped") {
                val result = invalidField.mapReason(mapper)

                result shouldBe Invalid("length", "UPDATE: plouf!")
            }
        }
        describe("concat") {

            val usesCases = table(
                headers("case", "a", "b", "expected"),
                row("invalid + valid", invalid, valid, invalid),
                row("invalidField + valid", invalidField, valid, invalidField),
                row(
                    "invalidField + invalid",
                    invalidField,
                    invalid,
                    Invalid((invalidField as Invalid).reasons + (invalid as Invalid).reasons)
                ),
                row("valid + invalid", valid, invalid, invalid),
                row("valid + invalidField", valid, invalidField, invalidField),
                row(
                    "invalid + invalidField",
                    invalid,
                    invalidField,
                    Invalid(invalid.reasons + invalidField.reasons)
                )
            )

            forAll(usesCases) { case, a, b, expected ->
                it("$case should build $expected") {
                    val result = a concat b
                    result shouldBe expected
                }
            }
        }

        describe("toString") {
            val usesCases = table(
                headers("case", "a", "expected"),
                row("valid toString", valid, "Valid"),
                row("invalid toString", invalid, "Invalid: plaf!"),
                row("invalidField toString", invalidField, "Invalid: [length] plouf!"),
                row(
                    "(invalid + invalidField) toString", invalid concat invalidField, """Invalid:
                   | - plaf!
                   | - [length] plouf!""".trimMargin()
                )
            )

            forAll(usesCases) { case, a, expected ->
                it("$case should build $expected") {
                    val result = a.toString()
                    result shouldBe expected
                }
            }
        }
    }
}
