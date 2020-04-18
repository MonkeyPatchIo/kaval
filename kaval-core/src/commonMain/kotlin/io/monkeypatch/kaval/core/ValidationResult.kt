package io.monkeypatch.kaval.core

/**
 * Represent a validation result
 */
sealed class ValidationResult {

    /**
     * Map some [ValidationIssue]
     * Note: this is useful when creating custom validator, otherwise you should not need this method
     *
     * @param mapper the mapper
     * @return the validation result with mapped [ValidationIssue]
     */
    fun mapReason(mapper: (ValidationIssue) -> ValidationIssue): ValidationResult =
        when (this) {
            is Valid -> Valid
            is Invalid -> Invalid(
                reasons.map { mapper(it) })
        }

    /**
     * Merge two validation result
     * Notes:
     * - this method is symmetric and associative
     * - this may generate duplicated issues
     *
     * @param other the other validation result
     * @return a validation result with all issues
     */
    infix fun concat(other: ValidationResult): ValidationResult =
        when {
            this is Valid && other is Invalid -> other
            this is Invalid && other is Valid -> this
            this is Invalid && other is Invalid -> Invalid(this.reasons + other.reasons)
            else -> Valid
        }
}

/**
 * A successful validation
 */
object Valid : ValidationResult() {
    override fun toString(): String =
        "Valid"
}

/**
 * A failed validation
 *
 * @param reasons list (not empty) with all issues
 */
data class Invalid internal constructor(val reasons: List<ValidationIssue>) : ValidationResult() {

    /**
     * Issues
     */
    val issues: List<ValidationIssue>
        get() = reasons

    /**
     * Build failed validation with one issue on the element
     *
     * @param reason the failure explanation
     */
    constructor(reason: String) : this(listOf(BaseValidationIssue(reason)))

    /**
     * Build failed validation with one issue on a field of an element
     *
     * @param field the field name
     * @param reason the failure explanation
     */
    constructor(field: String, reason: String) : this(listOf(FieldValidationIssue(field, reason)))

    init {
        require(reasons.isNotEmpty()) { "Invalid requires at least one reason" }
    }

    override fun toString(): String =
        if (reasons.size <= 1) "Invalid: ${reasons[0]}"
        else "Invalid:\n" + reasons.joinToString("\n").prependIndent(" - ")
}

/**
 * Represent one validation issue
 *
 */
sealed class ValidationIssue {
    abstract val message: String
}

/**
 * Represent one validation issue on an Element
 *
 * @param message an explanation
 */
data class BaseValidationIssue(override val message: String) : ValidationIssue() {
    override fun toString(): String =
        message
}

/**
 * Represent one validation issue on a field of an Element
 *
 * @param field the field
 * @param message an explanation
 */
data class FieldValidationIssue(val field: String, override val message: String) : ValidationIssue() {
    override fun toString(): String =
        "[$field] $message"
}
