package io.monkeypatch.kaval.core

/**
 * An always valid validator
 * Could be useful when combining validators
 *
 * @param T the element type
 * @return an always valid validator
 */
fun <T> alwaysValid(): Validator<T> =
    validator { Valid }

/**
 * An always invalid validator
 * Could be useful when combining validators
 *
 * @param reason the explanation
 * @param T the element type
 * @return an always invalid validator
 */
fun <T> alwaysInvalid(reason: String): Validator<T> =
    validator { Invalid(reason) }

/**
 * Validate an element with a predicate
 *
 * @param predicate if the predicate return `false`, the element is invalid
 * @param reason an explanation message builder
 * @param T the element type
 * @return a validator based on a predicate
 */
fun <T> predicate(predicate: (T) -> Boolean, reason: (T) -> String): Validator<T> =
    validator {
        if (predicate(it)) Valid
        else Invalid(reason(it))
    }

/**
 * Validate if the element is not null
 * Could be useful when using Java code
 *
 * @param T the element type
 * @return a validator that reject null
 */
fun <T> notNull(): Validator<T?> =
    predicate({ it != null }) { "requires to be not null" }

/**
 * Validate if the element is into an iterable
 *
 * @param T the element type
 * @param iter the iterable, e.g. a [List], a [Set], ...
 * @return a validator that check the element is into the iterable
 */
fun <T> containsBy(iter: Iterable<T>): Validator<T> =
    predicate({ iter.contains(it) }) {
        val list = iter.joinToString(separator = ", ", prefix = "{", postfix = "}", limit = 4)
        "requires to be in $list, got $it"
    }

/**
 * Validate if the element is not into an iterable
 *
 * @param T the element type
 * @param iter the iterable, e.g. a [List], a [Set], ...
 * @return a validator that check the element is **not** into the iterable
 */
fun <T> notContainsBy(iter: Iterable<T>): Validator<T> =
    predicate({ !iter.contains(it) }) {
        val list = iter.joinToString(separator = ", ", prefix = "{", postfix = "}", limit = 4)
        "requires not to be in $list, got $it"
    }

/**
 * Validate an element if it's not null
 *
 * Example:
 *
 * ```
 * val validator: Validator<String?> = nullOr { matches<String>(Regex("pl.*p")) }
 *
 * validator.validate(null) // Valid
 * validator.validate("foobar") // Invalid: requires matching 'pl.*p'
 * validator.validate("plop") // Valid
 * ```
 *
 * @param validator the validator of a not null element
 * @param T the element type
 * @return a validator of a nullable element
 */
fun <T> nullOr(validator: () -> Validator<T>): Validator<T?> =
    validator { t ->
        if (t != null) validator().validate(t)
        else Valid
    }

/**
 * Validate a field of an element
 * Note: no reflexion used here, if you want to use validation, check the `property` validator into `kaval-reflect`
 *
 * @param fieldName the field name
 * @param fieldExtractor extractor of the field
 * @param fieldValidator validator for the field
 * @return a validator that focus on a field of an element
 */
fun <H, C> field(fieldName: String, fieldExtractor: (H) -> C, fieldValidator: () -> Validator<C>): Validator<H> =
    validator { host: H ->
        val child = fieldExtractor(host)
        val childValidation = fieldValidator().validate(child)
        childValidation.mapReason { reason ->
            when (reason) {
                is FieldValidationIssue ->
                    FieldValidationIssue("$fieldName.${reason.field}", reason.message)
                is BaseValidationIssue ->
                    FieldValidationIssue(fieldName, reason.message)
            }
        }
    }

/**
 * Validate is Instance
 *
 * @param T the element type
 * @param U the expected type, should be a subclass of `T`
 * @return a validator that checks if the element is instance of type `U`
 */
inline fun <T, reified U : T> isInstance(): Validator<T> =
    predicate({ it is U }) { "requires to be a ${U::class}" }

/**
 * Validate with a more specific validator
 *
 * Example:
 * ```
 * sealed class HttpStatus {
 *   abstract val statusCode: Int
 *   data class Ok(override val statusCode: Int) : HttpStatus()
 *   data class Error(override val statusCode: Int) : HttpStatus()
 *   data class ServerError(override val statusCode: Int) : HttpStatus()
 * }
 *
 * val validator: Validator<HttpStatus> =
 *   whenIsInstance<HttpStatus, HttpStatus.Ok> { // Only check when is an instance of HttpStatus.Ok
 *     field("value", HttpStatus.Ok::statusCode) { inRange(200..299) }
 *   } and whenIsInstance {
 *     field("value", HttpStatus.Error::statusCode) { inRange(400..499) }
 *   } and whenIsInstance {
 *     field("value", HttpStatus.ServerError::statusCode) { inRange(500..599) }
 *   }
 *
 * validator.validate(HttpStatus.Ok(201)) // Valid
 * validator.validate(HttpStatus.Error(201)) // Invalid: [value] requires to be in range 400..499, got 201
 * ```
 *
 * @param uValidator a validator for an element of type `U`
 * @param T the element type
 * @param U the expected type, should be a subclass of `T`
 * @return a validator that check if the element is instance of type `U`, then use the uValidator, otherwise it's `Valid`
 */
inline fun <T, reified U : T> whenIsInstance(crossinline uValidator: () -> Validator<U>): Validator<T> =
    validator { t ->
        if (t !is U) Valid
        else uValidator().validate(t).mapReason { reason ->
            when (reason) {
                is FieldValidationIssue ->
                    FieldValidationIssue(reason.field, reason.message)
                is BaseValidationIssue ->
                    BaseValidationIssue(reason.message)
            }
        }
    }
