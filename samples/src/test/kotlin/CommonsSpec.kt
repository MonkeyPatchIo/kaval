import io.kotlintest.should
import io.kotlintest.specs.DescribeSpec
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class CommonsSpec : DescribeSpec() {

    init {

        describe("plopValidator") {

            it("should accept null") {
                val result = plopValidator.validate(null)
                result should beValid()
            }

            it("should reject foobar") {
                val result = plopValidator.validate("foobar")
                result should beInvalidWithReason(
                    "requires matching 'pl.*p'"
                )
            }

            it("should accept plop") {
                val result = plopValidator.validate("plop")
                result should beValid()
            }
        }
    }
}
