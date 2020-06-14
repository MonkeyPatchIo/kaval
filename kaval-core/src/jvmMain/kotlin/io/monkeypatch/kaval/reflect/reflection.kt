package io.monkeypatch.kaval.reflect

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.and
import io.monkeypatch.kaval.core.field
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * Validate a field of an element
 * Note: no reflexion used here, if you want to use validation, check the `property` validator into `kaval-reflect`
 *
 * @param property element property to validate, e.g `String::length`
 * @param childValidator validator for the field
 * @return a validator that focus on a field of an element
 */
fun <H, C> property(property: KProperty<C>, childValidator: () -> Validator<C>): Validator<H> =
    field(property.name, { property.getter.call(it) }, childValidator)

/**
 * Provide an easy DSL to create a validator based on properties
 *
 * @param block receiver to declare properties validator
 * @return a validator based on properties
 */
inline fun <reified T : Any> reflectValidator(block: ReflectValidatorDsl<T>.() -> Unit): Validator<T> =
    ReflectValidatorDsl<T>()
        .apply(block)
        .build()

/**
 * **DSL**, Provide a context for building a validator based on an object properties
 */
class ReflectValidatorDsl<T> {
    private val propertyValidators = mutableListOf<Validator<T>>()

    /**
     * Register a property validator
     */
    operator fun <U> KProperty1<T, U>.invoke(block: () -> Validator<U>) {
        propertyValidators.add(property(this, block))
    }

    /**
     * Build the validator
     */
    fun build(): Validator<T> =
        propertyValidators.reduce { acc, validator ->
            acc and validator
        }
}
