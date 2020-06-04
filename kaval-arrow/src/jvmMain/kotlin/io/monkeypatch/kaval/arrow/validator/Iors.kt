package io.monkeypatch.kaval.arrow.validator

import arrow.core.Either
import arrow.core.Ior
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.and
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.predicate
import io.monkeypatch.kaval.core.whenIsInstance

/**
 * Provide validators for ArrowKt [Ior]
 */
object Iors {

    /**
     * A validator for [Ior] that only accept [Ior.Left]
     *
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> isLeft(): Validator<Ior<L, R>> =
        predicate(Ior<L, R>::isLeft) { "require to be left" }

    /**
     * A validator for [Ior] that only accept [Ior.Left] when valid
     *
     * @param validator provide the left validator
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> isLeft(validator: () -> Validator<L>): Validator<Ior<L, R>> =
        isLeft<L, R>() and whenIsInstance {
            field("value", Ior.Left<L>::value, validator)
        }

    /**
     * A validator for [Either] that only accept [Ior.Right]
     *
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> isRight(): Validator<Ior<L, R>> =
        predicate(Ior<L, R>::isRight) { "require to be right" }

    /**
     * A validator for [Either] that only accept [Ior.Right] when valid
     *
     * @param validator provide the right validator
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> isRight(validator: () -> Validator<R>): Validator<Ior<L, R>> =
        isRight<L, R>() and whenIsInstance {
            field("value", Ior.Right<R>::value, validator)
        }

    /**
     * A validator for [Either] that only accept [Ior.Both]
     *
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> isBoth(): Validator<Ior<L, R>> =
        predicate(Ior<L, R>::isBoth) { "require to be both" }

    /**
     * A validator for [Either] that only accept [Ior.Right] when valid
     *
     * @param leftValidator provide a validator for [Ior.Left] cases
     * @param rightValidator provide a validator for [Ior.Right] cases
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> isBoth(leftValidator: () -> Validator<L>, rightValidator: () -> Validator<R>): Validator<Ior<L, R>> =
        isBoth<L, R>() and whenIsInstance {
            field("leftValue", Ior.Both<L, R>::leftValue, leftValidator) and
                field("rightValue", Ior.Both<L, R>::rightValue, rightValidator)
        }

    /**
     * A validator for [Either] that use validator for left and right cases
     *
     * @param leftValidator provide a validator for [Ior.Left] cases
     * @param rightValidator provide a validator for [Ior.Right] cases
     * @param L the ior left type
     * @param R the ior right type
     * @return the validator
     */
    fun <L, R> validator(leftValidator: () -> Validator<L>, rightValidator: () -> Validator<R>): Validator<Ior<L, R>> =
        { ior ->
            ior.fold(
                { a -> leftValidator()(a) },
                { b -> rightValidator()(b) },
                { a, b ->
                    leftValidator()(a) concat rightValidator()(b)
                }
            )
        }
}
