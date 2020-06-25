package io.monkeypatch.kaval.coroutine

import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationIssue.BaseValidationIssue
import io.monkeypatch.kaval.core.ValidationIssue.FieldValidationIssue

/**
 * Validate an element with a predicate
 *
 * @param predicate if the predicate return `false`, the element is invalid
 * @param reason an explanation message builder
 * @param T the element type
 * @return a validator based on a predicate
 */
fun <T> predicate(predicate: suspend (T) -> Boolean, reason: (T) -> String): SuspendValidator<T> =
    {
        if (predicate(it)) Valid
        else Invalid(reason(it))
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
fun <T> nullOr(validator: suspend () -> SuspendValidator<T>): SuspendValidator<T?> =
    { t ->
        if (t != null) validator()(t)
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
fun <H, C> field(
    fieldName: String,
    fieldExtractor: suspend (H) -> C,
    fieldValidator: suspend () -> SuspendValidator<C>
): SuspendValidator<H> =
    { host: H ->
        val child = fieldExtractor(host)
        val childValidation = fieldValidator()(child)
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
 * Validate with a more specific validator
 *
 * @param uValidator a validator for an element of type `U`
 * @param T the element type
 * @param U the expected type, should be a subclass of `T`
 * @return a validator that check if the element is instance of type `U`, then use the uValidator, otherwise it's `Valid`
 */
inline fun <T, reified U : T> whenIsInstance(crossinline uValidator: suspend () -> SuspendValidator<U>): SuspendValidator<T> =
    { t ->
        if (t !is U) Valid
        else uValidator()(t).mapReason { reason ->
            when (reason) {
                is FieldValidationIssue ->
                    FieldValidationIssue(reason.field, reason.message)
                is BaseValidationIssue ->
                    BaseValidationIssue(reason.message)
            }
        }
    }
