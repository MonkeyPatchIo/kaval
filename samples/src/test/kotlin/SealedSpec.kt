import io.kotlintest.should
import io.kotlintest.specs.DescribeSpec
import io.monkeypatch.kaval.kotlintest.beInvalid
import io.monkeypatch.kaval.kotlintest.beValid

class SealedSpec : DescribeSpec() {

    init {

        describe("HttpStatus.validator") {
            val validator = HttpStatus.validator

            it("should accept Ok(200)") {
                val result = validator.validate(HttpStatus.Ok(200))
                result should beValid()
            }

            it("should accept Error(401)") {
                val result = validator.validate(HttpStatus.Error(401))
                result should beValid()
            }

            it("should accept ServerError(503)") {
                val result = validator.validate(HttpStatus.ServerError(503))
                result should beValid()
            }

            it("should reject Ok(400)") {
                val result = validator.validate(HttpStatus.Ok(400))
                result should beInvalid()
            }

            it("should reject Error(201)") {
                val result = validator.validate(HttpStatus.Error(201))
                result should beInvalid()
            }

            it("should reject ServerError(413)") {
                val result = validator.validate(HttpStatus.ServerError(413))
                result should beInvalid()
            }
        }
    }
}
