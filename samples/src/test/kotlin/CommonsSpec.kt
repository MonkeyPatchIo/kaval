import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
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
