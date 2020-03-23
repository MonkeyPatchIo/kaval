package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.predicate

/**
 * Validators for [Double]
 */
object Doubles {
    /**
     * A validator for _in closed range_ with [Double]
     *
     * @return a validator for `in`
     */
    fun inRange(range: ClosedRange<Double>): Validator<Double> =
        predicate({ it in range }) { "requires to be in range $range, got $it" }

    /**
     * A validator for [Double] strictly positive
     *
     * @return a validator for `> 0 `
     */
    val strictlyPositive: Validator<Double> =
        predicate({ it > 0 }) { "requires to be strictly positive" }

    /**
     * A validator for [Double] strictly positive
     *
     * @return a validator for `>= 0 `
     */
    val positive: Validator<Double> =
        predicate({ it >= 0 }) { "requires to be positive" }

    /**
     * A validator for [Double] strictly negative
     *
     * @return a validator for `> 0 `
     */
    val strictlyNegative: Validator<Double> =
        predicate({ it < 0 }) { "requires to be strictly negative" }

    /**
     * A validator for [Double] strictly negative
     *
     * @return a validator for `>= 0 `
     */
    val negative: Validator<Double> =
        predicate({ it <= 0 }) { "requires to be negative" }
}
