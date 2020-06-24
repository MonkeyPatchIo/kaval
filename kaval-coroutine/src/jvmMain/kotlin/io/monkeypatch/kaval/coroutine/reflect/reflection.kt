package io.monkeypatch.kaval.coroutine.reflect

import io.monkeypatch.kaval.coroutine.SuspendValidator
import io.monkeypatch.kaval.coroutine.and
import io.monkeypatch.kaval.coroutine.field
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
fun <H, C> property(property: KProperty<C>, childValidator: suspend () -> SuspendValidator<C>): SuspendValidator<H> =
    field(property.name, { property.getter.call(it) }, childValidator)

/**
 * Provide an easy DSL to create a validator based on properties
 *
 * @param block receiver to declare properties validator
 * @return a validator based on properties
 */
inline fun <reified T : Any> reflectValidator(block: ReflectSuspendValidatorDsl<T>.() -> Unit): SuspendValidator<T> =
    ReflectSuspendValidatorDsl<T>()
        .apply(block)
        .build()

/**
 * **DSL**, Provide a context for building a validator based on an object properties
 */
class ReflectSuspendValidatorDsl<T> {
    private val propertyValidators = mutableListOf<SuspendValidator<T>>()

    /**
     * Register a property suspend validator
     */
    operator fun <U> KProperty1<T, U>.invoke(block: suspend () -> SuspendValidator<U>) {
        propertyValidators.add(property(this, block))
    }

    /**
     * Build the validator
     */
    fun build(): SuspendValidator<T> =
        propertyValidators.reduce { acc, validator ->
            acc and validator
        }
}
