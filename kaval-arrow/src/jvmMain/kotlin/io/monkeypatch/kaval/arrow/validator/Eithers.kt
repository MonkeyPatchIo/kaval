package io.monkeypatch.kaval.arrow.validator

import arrow.core.Either
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.and
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.predicate
import io.monkeypatch.kaval.core.whenIsInstance

/**
 * Provide validators for ArrowKt [Either]
 */
object Eithers {

    /**
     * A validator for [Either] that only accept [Either.Left]
     *
     * @param L the either left type
     * @param R the either right type
     * @return the validator
     */
    fun <L, R> isLeft(): Validator<Either<L, R>> =
        predicate(Either<L, R>::isLeft) { "require to be left" }

    /**
     * A validator for [Either] that only accept [Either.Left] when valid
     *
     * @param validator provide the left validator
     * @param L the either left type
     * @param R the either right type
     * @return the validator
     */
    fun <L, R> isLeft(validator: () -> Validator<L>): Validator<Either<L, R>> =
        isLeft<L, R>() and whenIsInstance {
            field("a", Either.Left<L>::a, validator)
        }

    /**
     * A validator for [Either] that only accept [Either.Right]
     *
     * @param L the either left type
     * @param R the either right type
     * @return the validator
     */
    fun <L, R> isRight(): Validator<Either<L, R>> =
        predicate(Either<L, R>::isRight) { "require to be right" }

    /**
     * A validator for [Either] that only accept [Either.Right] when valid
     *
     * @param validator provide the right validator
     * @param L the either left type
     * @param R the either right type
     * @return the validator
     */
    fun <L, R> isRight(validator: () -> Validator<R>): Validator<Either<L, R>> =
        isRight<L, R>() and whenIsInstance {
            field("b", Either.Right<R>::b, validator)
        }

    /**
     * A validator for [Either] that use validator for left and right cases
     *
     * @param leftValidator provide a validator for [Either.Left] cases
     * @param rightValidator provide a validator for [Either.Right] cases
     * @param L the either left type
     * @param R the either right type
     * @return the validator
     */
    fun <L, R> validator(
        leftValidator: () -> Validator<L>,
        rightValidator: () -> Validator<R>
    ): Validator<Either<L, R>> =
        { either ->
            either.fold(
                { leftValidator()(it) },
                { rightValidator()(it) }
            )
        }
}
