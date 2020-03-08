package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.predicate

/**
 * Validators for [Float]
 */
object Floats {
    /**
     * A validator for _in closed range_ with [Float]
     *
     * @return a validator for `in`
     */
    fun inRange(range: ClosedRange<Float>): Validator<Float> =
        predicate({ it in range }) { "require to be in range $range, got $it" }

    /**
     * A validator for [Float] strictly positive
     *
     * @return a validator for `> 0 `
     */
    val strictlyPositive: Validator<Float> =
        predicate({ it > 0 }) { "require to be strictly positive" }

    /**
     * A validator for [Float] strictly positive
     *
     * @return a validator for `>= 0 `
     */
    val positive: Validator<Float> =
        predicate({ it >= 0 }) { "require to be positive" }

    /**
     * A validator for [Float] strictly negative
     *
     * @return a validator for `> 0 `
     */
    val strictlyNegative: Validator<Float> =
        predicate({ it < 0 }) { "require to be strictly negative" }

    /**
     * A validator for [Float] strictly negative
     *
     * @return a validator for `>= 0 `
     */
    val negative: Validator<Float> =
        predicate({ it <= 0 }) { "require to be negative" }
}
