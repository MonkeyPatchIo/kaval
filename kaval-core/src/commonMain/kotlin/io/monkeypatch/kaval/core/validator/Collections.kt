package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.BaseValidationIssue
import io.monkeypatch.kaval.core.FieldValidationIssue
import io.monkeypatch.kaval.core.Invalid
import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationResult
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.predicate
import io.monkeypatch.kaval.core.validator
import io.monkeypatch.kaval.core.validator.Comparables.equalsTo
import io.monkeypatch.kaval.core.validator.Comparables.greaterOrEqualTo
import io.monkeypatch.kaval.core.validator.Comparables.lowerOrEqualTo

/**
 * Validators for [Collection] or [Map]
 */
object Collections {

    private const val truncateAfter: Int = 3

    /**
     * Validate a [Collection] that should **be** empty
     *
     * @param T the element type
     * @return the collection validator
     */
    fun <T> empty(): Validator<Collection<T>> =
        predicate({ it.isEmpty() }) {
            val content = it.joinToString(prefix = "{", postfix = "}", limit = truncateAfter)
            "require an empty collection, got $content"
        }

    /**
     * Validate a [Collection] that should **be not** empty
     *
     * @param T the element type
     * @return the collection validator
     */
    fun <T> notEmpty(): Validator<Collection<T>> =
        predicate({ it.isNotEmpty() }) { "require a not empty collection" }

    /**
     * A validator check size of a [Collection]
     *
     * @param T the element type
     * @param size the expected size
     * @return the collection validator
     */
    fun <T> hasSize(size: Int): Validator<Collection<T>> =
        field("size", Collection<T>::size) { equalsTo(size) }

    /**
     * A validator check maximum size of a [Collection]
     *
     * @param T the element type
     * @param size the maximum size
     * @return the collection validator
     */
    fun <T> maxSize(size: Int): Validator<Collection<T>> =
        field("size", Collection<T>::size) { lowerOrEqualTo(size) }

    /**
     * A validator check minimum size of a [Collection]
     *
     * @param T the element type
     * @param size the minimum size
     * @return the collection validator
     */
    fun <T> minSize(size: Int): Validator<Collection<T>> =
        field("size", Collection<T>::size) { greaterOrEqualTo(size) }

    /**
     * Validate an [Iterable] if all elements are valid
     * If the [Iterable] is empty, it is valid
     *
     * @param T the element type
     * @param elementValidator the element validator
     * @return the iterable validator
     */
    fun <T> allValid(elementValidator: () -> Validator<T>): Validator<Iterable<T>> =
        validator { iterable ->
            val validator = elementValidator()
            iterable.mapIndexed { index, elt ->
                val result = validator.validate(elt)
                // Use index as a field name
                result.mapReason { reason ->
                    val field = "$index" + if (reason is FieldValidationIssue) ".${reason.field}" else ""
                    FieldValidationIssue(field, reason.message)
                }
            }.fold(Valid, ValidationResult::concat)
        }

    /**
     * Validate an [Iterable] that require at least one element valid
     * If the [Iterable] is empty, it is not valid
     *
     * @param T the element type
     * @param elementValidator the element validator
     * @return the iterable validator
     */
    fun <T> atLeastOneValid(elementValidator: () -> Validator<T>): Validator<Iterable<T>> =
        validator { it.atLeastOneValid("element", "elements", elementValidator()) }

    /**
     * Validate a [Map] if all values are valid
     *
     * @param K the key type
     * @param V the value type
     * @param valueValidator the value validator
     * @return the map validator
     */
    fun <K, V> allValuesValid(valueValidator: () -> Validator<V>): Validator<Map<K, V>> =
        validator { map ->
            val validator = valueValidator()
            map.map { (key, value) ->
                validator.validate(value)
                    // Handle key as a field name
                    .mapReason { reason ->
                        val field = "$key" + if (reason is FieldValidationIssue) ".${reason.field}" else ""
                        FieldValidationIssue(field, reason.message)
                    }
            }.fold(Valid, ValidationResult::concat)
        }

    /**
     * Validate a [Map] if all keys are valid
     *
     * @param K the key type
     * @param V the value type
     * @param keyValidator the value validator
     * @return the map validator
     */
    fun <K, V> allKeysValid(keyValidator: () -> Validator<K>): Validator<Map<K, V>> =
        validator { map ->
            val validator = keyValidator()
            map.keys.map { key ->
                validator.validate(key)
                    // Handle key as a field name
                    .mapReason { reason ->
                        val field = "$key" + if (reason is FieldValidationIssue) ".${reason.field}" else ""
                        FieldValidationIssue(field, reason.message)
                    }
            }.fold(Valid, ValidationResult::concat)
        }

    /**
     * Validate a [Map.Entry] if key and value are valid
     *
     * @param K the key type
     * @param V the value type
     * @param keyValidator the key validator
     * @param valueValidator the value validator
     * @return the entry validator
     */
    fun <K, V> entryValidator(keyValidator: Validator<K>, valueValidator: Validator<V>): Validator<Map.Entry<K, V>> =
        field("key", Map.Entry<K, V>::key) {
            keyValidator
        } and field("value", Map.Entry<K, V>::value) {
            valueValidator
        }

    /**
     * Validate a [Map] if all entries are valid
     *
     * @param K the key type
     * @param V the value type
     * @param entryValidator the entry validator
     * @return the map validator
     */
    fun <K, V> allEntriesValid(entryValidator: () -> Validator<Map.Entry<K, V>>): Validator<Map<K, V>> =
        validator { map ->
            val validator = entryValidator()
            map.map { entry ->
                validator.validate(entry)
                    // Handle key as a field name
                    .mapReason { reason ->
                        val field = "${entry.key}" + if (reason is FieldValidationIssue) ".${reason.field}" else ""
                        FieldValidationIssue(field, reason.message)
                    }
            }.fold(Valid, ValidationResult::concat)
        }

    /**
     * Validate a [Map] that require at least one value valid
     * If the [Map] is empty, it is not valid
     *
     * @param K the key type
     * @param V the value type
     * @param valueValidator the value validator
     * @return the iterable validator
     */
    fun <K, V> atLeastOneValueValid(valueValidator: () -> Validator<V>): Validator<Map<K, V>> =
        validator { map ->
            map.values.atLeastOneValid("value", "values", valueValidator())
                .mapReason { reason ->
                    when (reason) {
                        is FieldValidationIssue -> FieldValidationIssue(reason.field, reason.message)
                        is BaseValidationIssue -> BaseValidationIssue(reason.message)
                    }
                }
        }

    /**
     * Validate a [Map] that require at least one key valid
     * If the [Map] is empty, it is not valid
     *
     * @param K the key type
     * @param V the value type
     * @param keyValidator the key validator
     * @return the iterable validator
     */
    fun <K, V> atLeastOneKeyValid(keyValidator: () -> Validator<K>): Validator<Map<K, V>> =
        validator { map ->
            map.keys.atLeastOneValid("key", "keys", keyValidator())
                .mapReason { reason ->
                    when (reason) {
                        is FieldValidationIssue -> FieldValidationIssue(reason.field, reason.message)
                        is BaseValidationIssue -> BaseValidationIssue(reason.message)
                    }
                }
        }

    /**
     * Validate a [Map] that require at least one value valid
     * If the [Map] is empty, it is not valid
     *
     * @param K the key type
     * @param V the value type
     * @param entryValidator the element validator
     * @return the iterable validator
     */
    fun <K, V> atLeastOneEntryValid(entryValidator: () -> Validator<Map.Entry<K, V>>): Validator<Map<K, V>> =
        validator { map ->
            map.entries.atLeastOneValid("entry", "entries", entryValidator())
                .mapReason { reason ->
                    when (reason) {
                        is FieldValidationIssue -> FieldValidationIssue(reason.field, reason.message)
                        is BaseValidationIssue -> BaseValidationIssue(reason.message)
                    }
                }
        }

    // Helpers
    private fun <T> Iterable<T>.atLeastOneValid(
        name: String,
        names: String,
        validator: Validator<T>
    ): ValidationResult {
        val iterator = iterator()
        var invalid: ValidationResult = Valid
        for (elt in iterator) {
            val eltResult = validator.validate(elt)
            if (eltResult == Valid) return Valid

            invalid = invalid concat eltResult
        }

        return when (invalid) {
            is Valid -> Invalid("require at least one $name valid, got no $names")
            is Invalid -> {
                val causes =
                    if (invalid.reasons.size == 1) invalid.reasons[0].toString()
                    else invalid.reasons.joinToString(prefix = "\n", separator = "\n", limit = truncateAfter) {
                        when (it) {
                            is FieldValidationIssue -> "[${it.field}] ${it.message}"
                            is BaseValidationIssue -> it.message
                        }
                    }.prependIndent(" ")
                Invalid("*", "require at least one $name valid, all $names are invalid: $causes")
            }
        }
    }
}
