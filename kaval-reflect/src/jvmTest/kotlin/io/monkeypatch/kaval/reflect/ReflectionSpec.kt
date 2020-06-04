package io.monkeypatch.kaval.reflect

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Ints
import io.monkeypatch.kaval.core.validator.Strings.notBlank
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class ReflectionSpec : DescribeSpec() {

    data class MyPojo(val name: String, val value: Int)

    init {

        describe("property") {
            val validator: Validator<MyPojo> = property(MyPojo::name) { notBlank }

            it("property should reject if field invalid") {
                val pojo = MyPojo("", 42)

                val result = validator(pojo)

                result should beInvalidWithReason("requires to be not blank")
            }

            it("property should accept if field valid") {
                val pojo = MyPojo("test", 42)

                val result = validator(pojo)

                result should beValid()
            }
        }

        describe("dsl") {
            val validator: Validator<MyPojo> = reflectValidator {
                MyPojo::name { notBlank }
                MyPojo::value { Ints.inRange(1..10) }
            }

            it("property should reject if name invalid") {
                val pojo = MyPojo("", 42)

                val result = validator(pojo)

                result should beInvalidWithReason("requires to be not blank")
            }

            it("property should reject if value invalid") {
                val pojo = MyPojo("test", 42)

                val result = validator(pojo)

                result should beInvalidWithReason("requires to be in range 1..10, got 42")
            }

            it("property should accept if field valid") {
                val pojo = MyPojo("test", 7)

                val result = validator(pojo)

                result should beValid()
            }
        }
    }
}
