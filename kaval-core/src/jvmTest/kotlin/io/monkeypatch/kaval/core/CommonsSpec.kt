package io.monkeypatch.kaval.core

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
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.monkeypatch.kaval.core.validator.Strings.notBlank
import io.monkeypatch.kaval.kotlintest.beInvalid
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class CommonsSpec : DescribeSpec() {

    interface UpperInterface
    object BaseImplementation : UpperInterface
    object AnotherImplementation : UpperInterface

    data class MyPojo(val name: String, val value: Int)

    private val any = Arb.choice(
        Arb.string(),
        Arb.int(),
        Arb.double(),
        Arb.bool(),
        Arb.constant(null)
    )

    init {
        describe("alwaysValid") {
            it("alwaysValid should always be Valid") {
                checkAll(any) { a ->
                    val validator = alwaysValid<Any?>()
                    validator.validate(a) should beValid()
                }
            }
        }

        describe("alwaysInvalid") {
            it("alwaysInvalid should always be Invalid") {
                checkAll(any) { a ->
                    val reason = "plaf!"
                    val validator = alwaysInvalid<Any?>(reason)
                    validator.validate(a) should beInvalidWithReason(
                        reason
                    )
                }
            }
        }

        describe("predicate") {
            val reason = "plop"

            it("predicate should be valid if true") {
                val validator = predicate<Any?>({ true }) { reason }
                checkAll(any) { a ->
                    val result = validator.validate(a)

                    result should beValid()
                }
            }

            it("predicate should be valid if false") {
                val validator = predicate<Any?>({ false }) { reason }
                checkAll(any) { a ->
                    val result = validator.validate(a)

                    result should beInvalidWithReason(
                        reason
                    )
                }
            }
        }

        describe("containsBy") {
            val iterable = listOf(1, 3, 5, 7, 9)
            val validator = containsBy(iterable)

            it("containsBy should be valid when into the iterable") {
                checkAll(Arb.element(iterable)) { i ->
                    val result = validator.validate(i)

                    result should beValid()
                }
            }

            it("containsBy should be valid when not into the iterable") {
                checkAll(Arb.element(iterable)) { i ->
                    val result = validator.validate(i + 1)
                    result should beInvalidWithReason("requires to be in {1, 3, 5, 7, ...}, got ${i + 1}")
                } }
        }

        describe("notContainsBy") {
            val iterable = listOf(1, 3, 5, 7, 9)
            val validator = notContainsBy(iterable)

            it("notContainsBy should be valid when not into the iterable") {
                checkAll(Arb.element(iterable)) { i ->
                    val result = validator.validate(i + 1)

                    result should beValid()
                }
            }

            it("notContainsBy should be valid when into the iterable") {
                checkAll(Arb.element(iterable)) { i ->
                    val result = validator.validate(i)
                    result should beInvalidWithReason("requires not to be in {1, 3, 5, 7, ...}, got $i")
                } }
        }

        describe("notNull") {
            val validator = notNull<Any?>()

            it("notNull should be valid if not null") {
                checkAll(any.filterNot { it == null }) { a ->
                    val result = validator.validate(a)

                    result should beValid()
                }
            }

            it("notNull should be invalid if null") {
                val result = validator.validate(null)

                result should beInvalid()
            }
        }

        describe("nullOr") {
            val reason = "plaf!"
            val invalid = alwaysInvalid<Any?>(reason)
            val valid = alwaysValid<Any?>()

            val usesCases = table(
                headers("name", "generator", "validataor", "isValid"),
                row("null and valid", Arb.constant(null), valid, true),
                row("null and invalid", Arb.constant(null), invalid, true),
                row("not null and valid", any.filterNot { it == null }, valid, true),
                row("not null and invalid", any.filterNot { it == null }, invalid, false)
            )

            usesCases.forAll { name, gen, notNullValidator, isValid ->
                val validator = nullOr { notNullValidator }

                it("$name should be ${if (isValid) "valid" else "invalid"}") {
                    checkAll(gen) { a ->
                        val result = validator.validate(a)

                        if (isValid) result should beValid()
                        else result should beInvalidWithReason(
                            reason
                        )
                    }
                }
            }
        }

        describe("field") {
            val validator: Validator<MyPojo> = field("name", MyPojo::name) {
                notBlank
            }

            it("field should reject if field invalid") {
                val pojo = MyPojo("", 42)
                val result = validator.validate(pojo)
                result should beInvalidWithReason(
                    "requires to be not blank"
                )
            }

            it("field should accept if field valid") {
                val pojo = MyPojo("test", 42)
                val result = validator.validate(pojo)
                result should beValid()
            }
        }

        describe("isInstance") {
            val validator: Validator<UpperInterface> =
                isInstance<UpperInterface, BaseImplementation>()

            it("isInstance should reject if instance invalid") {
                val result = validator.validate(
                    AnotherImplementation
                )
                result should beInvalidWithReason("requires to be a ${BaseImplementation::class.java}")
            }

            it("isInstance should accept if instance valid") {
                val result = validator.validate(
                    BaseImplementation
                )
                result should beValid()
            }
        }

        describe("whenIsInstance") {
            val reason = "plaf!"
            val invalid = alwaysInvalid<BaseImplementation>(reason)
            val validator: Validator<UpperInterface> =
                whenIsInstance { invalid }

            it("whenIsInstance should accept if not instance") {
                val result = validator.validate(
                    AnotherImplementation
                )
                result should beValid()
            }

            it("whenIsInstance should reject if instance because of validator") {
                val result = validator.validate(
                    BaseImplementation
                )
                result should beInvalidWithReason(
                    reason
                )
            }
        }
    }
}
