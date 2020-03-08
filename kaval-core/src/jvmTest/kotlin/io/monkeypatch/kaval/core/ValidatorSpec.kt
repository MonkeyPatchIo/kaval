package io.monkeypatch.kaval.core

import io.kotlintest.properties.Gen.Companion.bool
import io.kotlintest.properties.Gen.Companion.constant
import io.kotlintest.properties.Gen.Companion.double
import io.kotlintest.properties.Gen.Companion.int
import io.kotlintest.properties.Gen.Companion.oneOf
import io.kotlintest.properties.Gen.Companion.string
import io.kotlintest.properties.assertAll
import io.kotlintest.should
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class ValidatorSpec : DescribeSpec() {

    private val any = oneOf(
        string(),
        int(),
        double(),
        bool(),
        constant(null)
    )

    init {

        describe("and") {
            val reason = "plaf!"
            val invalid = alwaysInvalid<Any?>(reason)
            val valid = alwaysValid<Any?>()

            val usesCases = table(
                headers("name", "first", "second", "isValid"),
                row("valid and valid", valid, valid, true),
                row("valid and invalid", valid, invalid, false),
                row("invalid and valid", invalid, valid, false),
                row("invalid and invalid", invalid, invalid, false)
            )

            forAll(usesCases) { name, first, second, isValid ->
                val validator = first and second

                it("$name should be ${if (isValid) "valid" else "invalid"}") {
                    assertAll(any) { a ->
                        val result = validator.validate(a)

                        if (isValid) result should beValid()
                        else result should beInvalidWithReason(
                            reason
                        )
                    }
                }
            }
        }

        describe("andThen") {
            val reason1 = "plaf!"
            val invalid1 = alwaysInvalid<Any?>(reason1)
            val reason2 = "plouf!"
            val invalid2 = alwaysInvalid<Any?>(reason2)
            val valid = alwaysValid<Any?>()

            val usesCases = table(
                headers("name", "first", "second", "reasons"),
                row("valid andThen valid", valid, valid, emptyList()),
                row("valid andThen invalid1", valid, invalid1, listOf(reason1)),
                row("invalid andThen valid", invalid1, valid, listOf(reason1)),
                row("invalid andThen invalid", invalid1, invalid2, listOf(reason1))
            )

            forAll(usesCases) { name, first, second, reasons ->
                val validator = first andThen second

                it("$name should be ${if (reasons.isEmpty()) "valid" else "invalid with ${reasons.joinToString(", ")}"}") {
                    assertAll(any) {
                        val result = validator.validate(any)
                        if (reasons.isEmpty()) result should beValid()
                        else reasons.forEach { reason ->
                            result should beInvalidWithReason(
                                reason
                            )
                        }
                    }
                }
            }
        }

        describe("or") {
            val reason = "plaf!"
            val invalid = alwaysInvalid<Any?>(reason)
            val valid = alwaysValid<Any?>()

            val usesCases = table(
                headers("name", "first", "second", "isValid"),
                row("valid or valid", valid, valid, true),
                row("valid or invalid", valid, invalid, true),
                row("invalid or valid", invalid, valid, true),
                row("invalid or invalid", invalid, invalid, false)
            )

            forAll(usesCases) { name, first, second, isValid ->
                val validator = first or second

                it("$name should be ${if (isValid) "valid" else "invalid"}") {
                    assertAll(any) { a ->
                        val result = validator.validate(a)

                        if (isValid) result should beValid()
                        else result should beInvalidWithReason(
                            reason
                        )
                    }
                }
            }
        }
    }
}
