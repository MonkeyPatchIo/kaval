import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class CommonsSpec : DescribeSpec() {

    init {

        describe("plopValidator") {

            it("should accept null") {
                val result = plopValidator(null)
                result should beValid()
            }

            it("should reject foobar") {
                val result = plopValidator("foobar")
                result should beInvalidWithReason(
                    "requires matching 'pl.*p'"
                )
            }

            it("should accept plop") {
                val result = plopValidator("plop")
                result should beValid()
            }
        }
    }
}
