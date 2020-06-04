// ktlint-disable filename
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.and
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.validator.Ints.inRange
import io.monkeypatch.kaval.core.whenIsInstance

/**
 * Let's define a sealed hierarchy for HttpStatus
 */
sealed class HttpStatus {

    data class Ok(val statusCode: Int) : HttpStatus()

    data class Error(val statusCode: Int) : HttpStatus()

    data class ServerError(val statusCode: Int) : HttpStatus()

    companion object {

        /**
         * Validate the status statusCode for each HttpStatus variant
         */
        val validator: Validator<HttpStatus> = whenIsInstance<HttpStatus, Ok> {
            // Only check when is an instance of HttpStatus.Ok
            field("statusCode", Ok::statusCode) { inRange(200..299) }
        } and whenIsInstance {
            // Only check when is an instance of HttpStatus.Error
            field("statusCode", Error::statusCode) { inRange(400..499) }
        } and whenIsInstance {
            // Only check when is an instance of HttpStatus.ServerError
            field("statusCode", ServerError::statusCode) { inRange(500..599) }
        }
    }
}
