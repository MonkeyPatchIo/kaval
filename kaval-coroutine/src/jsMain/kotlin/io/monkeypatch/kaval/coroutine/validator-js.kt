package io.monkeypatch.kaval.coroutine

import io.monkeypatch.kaval.core.Valid
import io.monkeypatch.kaval.core.ValidationResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

actual infix fun <T> SuspendValidator<T>.and(other: SuspendValidator<T>): SuspendValidator<T> =
    { t: T ->
        val that = this
        coroutineScope {
            val result1 = async { that(t) }
            val result2 = async { other(t) }
            result1.await() concat result2.await()
        }
    }

actual infix fun <T> SuspendValidator<T>.or(other: SuspendValidator<T>): SuspendValidator<T> =
    { t: T ->
        // We run validators in parallel, first valid stop the other
        val that = this
        coroutineScope {
            val tv = async { that(t) }
            val ov = async { other(t) }

            select<ValidationResult> {
                tv.onAwait {
                    if (it is Valid) {
                        ov.cancel()
                        Valid
                    } else ov.await()
                }
                ov.onAwait {
                    if (it is Valid) {
                        tv.cancel()
                        Valid
                    } else tv.await()
                }
            }
        }
    }
