package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.predicate

/**
 * Validators for [Long]
 */
object Longs {
    /**
     * A validator for _in closed range_ with [Long]
     *
     * @return a validator for `in`
     */
    fun inRange(range: LongRange): Validator<Long> =
        predicate({ it in range }) { "requires to be in range $range, got $it" }

    /**
     * A validator for [Long] strictly positive
     *
     * @return a validator for `> 0 `
     */
    val strictlyPositive: Validator<Long> =
        predicate({ it > 0 }) { "requires to be strictly positive" }

    /**
     * A validator for [Long] strictly positive
     *
     * @return a validator for `>= 0 `
     */
    val positive: Validator<Long> =
        predicate({ it >= 0 }) { "requires to be positive" }

    /**
     * A validator for [Long] strictly negative
     *
     * @return a validator for `> 0 `
     */
    val strictlyNegative: Validator<Long> =
        predicate({ it < 0 }) { "requires to be strictly negative" }

    /**
     * A validator for [Long] strictly negative
     *
     * @return a validator for `>= 0 `
     */
    val negative: Validator<Long> =
        predicate({ it <= 0 }) { "requires to be negative" }
}
