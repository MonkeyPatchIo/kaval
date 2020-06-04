import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.kotlintest.beInvalid
import io.monkeypatch.kaval.kotlintest.beValid

class SealedSpec : DescribeSpec() {

    init {

        describe("HttpStatus.validator") {
            val validator = HttpStatus.validator

            it("should accept Ok(200)") {
                val result = validator(HttpStatus.Ok(200))
                result should beValid()
            }

            it("should accept Error(401)") {
                val result = validator(HttpStatus.Error(401))
                result should beValid()
            }

            it("should accept ServerError(503)") {
                val result = validator(HttpStatus.ServerError(503))
                result should beValid()
            }

            it("should reject Ok(400)") {
                val result = validator(HttpStatus.Ok(400))
                result should beInvalid()
            }

            it("should reject Error(201)") {
                val result = validator(HttpStatus.Error(201))
                result should beInvalid()
            }

            it("should reject ServerError(413)") {
                val result = validator(HttpStatus.ServerError(413))
                result should beInvalid()
            }
        }
    }
}
