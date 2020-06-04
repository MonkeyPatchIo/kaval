package io.monkeypatch.kaval.core.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.should
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.kotlintest.beInvalidWithAllReasons
import io.monkeypatch.kaval.kotlintest.beValid

class TuplesSpec : DescribeSpec() {
    private val sValid = "Plop"
    private val sInvalid = " "
    private val sReason = "requires to be not blank"

    private val iValid = 42
    private val iInvalid = 0
    private val iReason = "requires to be strictly positive"

    private val lValid = 7L
    private val lInvalid = 0L
    private val lReason = "requires to be in range 5..10, got 0"

    init {
        describe("Tuples.pair") {
            val validator: Validator<Pair<String, Int>> = Tuples.pair(Strings.notBlank, Ints.strictlyPositive)
            val usesCases = table(
                headers("string", "int", "errors"),
                row(sValid, iValid, emptyList()),
                row(sValid, iInvalid, listOf(iReason)),
                row(sInvalid, iValid, listOf(sReason)),
                row(sInvalid, iInvalid, listOf(sReason, iReason))
            )

            forAll(usesCases) { s, i, expected ->
                it("($s, $i) should be ${if (expected.isEmpty()) "Valid" else "Invalid"}") {
                    val result = validator(s to i)

                    if (expected.isEmpty()) {
                        result should beValid()
                    } else {
                        val head = expected.first()
                        val tail = expected.toTypedArray()
                        result should beInvalidWithAllReasons(head, *tail)
                    }
                }
            }
        }

        describe("Tuples.triple") {
            val validator: Validator<Triple<String, Int, Long>> = Tuples.triple(
                Strings.notBlank,
                Ints.strictlyPositive,
                Longs.inRange(5L..10L)
            )
            val usesCases = table(
                headers("string", "int", "long", "errors"),
                row(sValid, iValid, lValid, emptyList()),
                row(sValid, iValid, lInvalid, listOf(lReason)),
                row(sValid, iInvalid, lValid, listOf(iReason)),
                row(sValid, iInvalid, lInvalid, listOf(iReason, lReason)),
                row(sInvalid, iValid, lValid, listOf(sReason)),
                row(sInvalid, iValid, lInvalid, listOf(sReason, lReason)),
                row(sInvalid, iInvalid, lValid, listOf(sReason, iReason)),
                row(sInvalid, iInvalid, lInvalid, listOf(sReason, iReason, lReason))
            )

            forAll(usesCases) { s, i, l, expected ->
                it("($s, $i, $l) should be ${if (expected.isEmpty()) "Valid" else "Invalid"}") {
                    val result = validator(Triple(s, i, l))

                    if (expected.isEmpty()) {
                        result should beValid()
                    } else {
                        val head = expected.first()
                        val tail = expected.toTypedArray()
                        result should beInvalidWithAllReasons(head, *tail)
                    }
                }
            }
        }
    }
}
