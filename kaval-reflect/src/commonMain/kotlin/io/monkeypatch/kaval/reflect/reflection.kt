package io.monkeypatch.kaval.reflect

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import kotlin.reflect.KProperty

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
