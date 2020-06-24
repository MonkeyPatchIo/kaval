package io.monkeypatch.kaval.coroutine

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest

@OptIn(ExperimentalCoroutinesApi::class)
class ValidatorSpec : DescribeSpec() {

    private val any = Arb.choice(
        Arb.string(),
        Arb.int(),
        Arb.double(),
        Arb.bool(),
        Arb.constant(null)
    )

    private val duration = 1000L // 1s
    private val reason = "plaf!"
    private val invalid: SuspendValidator<Any?> = {
        delay(duration)
        Invalid(reason)
    }
    private val valid: SuspendValidator<Any?> = {
        delay(duration)
        Valid
    }

    init {

        describe("and") {

            val usesCases = table(
                headers("name", "first", "second", "isValid"),
                row("valid and valid", valid, valid, true),
                row("valid and invalid", valid, invalid, false),
                row("invalid and valid", invalid, valid, false),
                row("invalid and invalid", invalid, invalid, false)
            )

            usesCases.forAll { name, first, second, isValid ->
                val validator = first and second

                it("$name should be ${if (isValid) "valid" else "invalid"}") {
                    checkAll(any) { a ->
                        runBlockingTest {
                            val result = validator(a)

                            if (isValid) result should beValid()
                            else result should beInvalidWithReason(reason)
                            currentTime shouldBe duration
                        }
                    }
                }
            }
        }

        describe("andThen") {
            val reason1 = "plaf!"
            val invalid1: SuspendValidator<Any?> = { Invalid(reason1) }
            val reason2 = "plouf!"
            val invalid2: SuspendValidator<Any?> = { Invalid(reason2) }
            val valid: SuspendValidator<Any?> = { Valid }

            val usesCases = table(
                headers("name", "first", "second", "reasons"),
                row("valid andThen valid", valid, valid, emptyList()),
                row("valid andThen invalid1", valid, invalid1, listOf(reason1)),
                row("invalid andThen valid", invalid1, valid, listOf(reason1)),
                row("invalid andThen invalid", invalid1, invalid2, listOf(reason1))
            )

            usesCases.forAll { name, first, second, reasons ->
                val validator = first andThen second

                it("$name should be ${if (reasons.isEmpty()) "valid" else "invalid with ${reasons.joinToString(", ")}"}") {
                    checkAll(any) {
                        val result = validator(any)
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

            val usesCases = table(
                headers("name", "first", "second", "isValid"),
                row("valid or valid", valid, valid, true),
                row("valid or invalid", valid, invalid, true),
                row("invalid or valid", invalid, valid, true),
                row("invalid or invalid", invalid, invalid, false)
            )

            usesCases.forAll { name, first, second, isValid ->
                val validator = first or second

                it("$name should be ${if (isValid) "valid" else "invalid"}") {
                    checkAll(any) { a ->
                        runBlockingTest {
                            val result = validator(a)

                            if (isValid) result should beValid()
                            else result should beInvalidWithReason(reason)
                            currentTime shouldBe duration
                        }
                    }
                }
            }
        }
    }
}
