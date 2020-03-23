package io.monkeypatch.kaval.reflect

import io.kotlintest.should
import io.kotlintest.specs.DescribeSpec
import io.monkeypatch.kaval.core.Validator
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

                val result = validator.validate(pojo)

                result should beInvalidWithReason("requires to be not blank")
            }

            it("property should accept if field valid") {
                val pojo = MyPojo("test", 42)

                val result = validator.validate(pojo)

                result should beValid()
            }
        }
    }
}
