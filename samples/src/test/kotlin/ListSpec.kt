import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class ListSpec : DescribeSpec() {

    init {

        describe("listValidator") {

            it("should accept empty list") {
                val result = listValidator.validate(emptyList())
                result should beValid()
            }

            it("should reject List(1,10,100)") {
                val result = listValidator.validate(listOf(1, 10, 100))
                result should beInvalidWithReason("requires to be greater than 9, got 1")
            }

            it("should accept List(10,100)") {
                val result = listValidator.validate(listOf(10, 100))
                result should beValid()
            }
        }
    }
}
