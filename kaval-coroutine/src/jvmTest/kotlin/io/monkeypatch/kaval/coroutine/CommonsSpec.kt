package io.monkeypatch.kaval.coroutine

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.should
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
import io.monkeypatch.kaval.core.validator.Strings
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class CommonsSpec : DescribeSpec() {

    interface UpperInterface
    object BaseImplementation : UpperInterface
    object AnotherImplementation : UpperInterface

    data class MyPojo(val name: String, val value: Int)

    @Suppress("UNCHECKED_CAST")
    private val any = Arb.choice(
        Arb.string() as Arb<Any>,
        Arb.int() as Arb<Any>,
        Arb.double() as Arb<Any>,
        Arb.bool() as Arb<Any>
    )

    @Suppress("UNCHECKED_CAST")
    private val anyNullable = Arb.choice(
        Arb.string() as Arb<Any?>,
        Arb.int() as Arb<Any?>,
        Arb.double() as Arb<Any?>,
        Arb.bool() as Arb<Any?>,
        Arb.constant(null) as Arb<Any?>
    )

    init {
        describe("predicate") {
            val reason = "plop"

            it("predicate should be valid if true") {
                val validator = predicate<Any?>({ true }) { reason }
                checkAll(anyNullable) { a ->
                    val result = validator(a)

                    result should beValid()
                }
            }

            it("predicate should be valid if false") {
                val validator = predicate<Any?>({ false }) { reason }
                checkAll(any) { a ->
                    val result = validator(a)

                    result should beInvalidWithReason(
                        reason
                    )
                }
            }
        }

        describe("nullOr") {
            val reason = "plaf!"
            val invalid: SuspendValidator<Any?> = { Invalid(reason) }
            val valid: SuspendValidator<Any?> = { Valid }

            val usesCases = table(
                headers("name", "generator", "validataor", "isValid"),
                row("null and valid", Arb.constant(null), valid, true),
                row("null and invalid", Arb.constant(null), invalid, true),
                row("not null and valid", any, valid, true),
                row("not null and invalid", any, invalid, false)
            )

            usesCases.forAll { name, gen, notNullValidator, isValid ->
                val validator = nullOr { notNullValidator }

                it("$name should be ${if (isValid) "valid" else "invalid"}") {
                    checkAll(gen) { a ->
                        val result = validator(a)

                        if (isValid) result should beValid()
                        else result should beInvalidWithReason(
                            reason
                        )
                    }
                }
            }
        }

        describe("field") {
            val validator: SuspendValidator<MyPojo> = field("name", MyPojo::name) {
                Strings.notBlank.toSuspend()
            }

            it("field should reject if field invalid") {
                val pojo = MyPojo("", 42)
                val result = validator(pojo)
                result should beInvalidWithReason(
                    "requires to be not blank"
                )
            }

            it("field should accept if field valid") {
                val pojo = MyPojo("test", 42)
                val result = validator(pojo)
                result should beValid()
            }
        }

        describe("whenIsInstance") {
            val reason = "plaf!"
            val invalid: SuspendValidator<BaseImplementation> = { Invalid(reason) }
            val validator: SuspendValidator<UpperInterface> =
                whenIsInstance { invalid }

            it("whenIsInstance should accept if not instance") {
                val result = validator(
                    AnotherImplementation
                )
                result should beValid()
            }

            it("whenIsInstance should reject if instance because of validator") {
                val result = validator(
                    BaseImplementation
                )
                result should beInvalidWithReason(
                    reason
                )
            }
        }
    }
}
