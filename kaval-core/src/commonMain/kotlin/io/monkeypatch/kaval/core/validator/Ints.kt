package io.monkeypatch.kaval.core.validator

import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.predicate

/**
 * Validators for [Int]
 */
object Ints {
    /**
     * A validator for _in closed range_ with [Int]
     *
     * @return a validator for `in`
     */
    fun inRange(range: IntRange): Validator<Int> =
        predicate({ it in range }) { "require to be in range $range, got $it" }

    /**
     * A validator for [Int] strictly positive
     *
     * @return a validator for `> 0 `
     */
    val strictlyPositive: Validator<Int> =
        predicate({ it > 0 }) { "require to be strictly positive" }

    /**
     * A validator for [Int] strictly positive
     *
     * @return a validator for `>= 0 `
     */
    val positive: Validator<Int> =
        predicate({ it >= 0 }) { "require to be positive" }

    /**
     * A validator for [Int] strictly negative
     *
     * @return a validator for `> 0 `
     */
    val strictlyNegative: Validator<Int> =
        predicate({ it < 0 }) { "require to be strictly negative" }

    /**
     * A validator for [Int] strictly negative
     *
     * @return a validator for `>= 0 `
     */
    val negative: Validator<Int> =
        predicate({ it <= 0 }) { "require to be negative" }
}
