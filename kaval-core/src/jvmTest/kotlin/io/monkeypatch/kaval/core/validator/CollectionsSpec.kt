package io.monkeypatch.kaval.core.validator

import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.specs.DescribeSpec
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Collections.allEntriesValid
import io.monkeypatch.kaval.core.validator.Collections.allKeysValid
import io.monkeypatch.kaval.core.validator.Collections.allValid
import io.monkeypatch.kaval.core.validator.Collections.allValuesValid
import io.monkeypatch.kaval.core.validator.Collections.atLeastOneEntryValid
import io.monkeypatch.kaval.core.validator.Collections.atLeastOneKeyValid
import io.monkeypatch.kaval.core.validator.Collections.atLeastOneValid
import io.monkeypatch.kaval.core.validator.Collections.atLeastOneValueValid
import io.monkeypatch.kaval.core.validator.Collections.empty
import io.monkeypatch.kaval.core.validator.Collections.entryValidator
import io.monkeypatch.kaval.core.validator.Collections.hasSize
import io.monkeypatch.kaval.core.validator.Collections.maxSize
import io.monkeypatch.kaval.core.validator.Collections.minSize
import io.monkeypatch.kaval.core.validator.Collections.notEmpty
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan
import io.monkeypatch.kaval.core.validator.Strings.notBlank
import io.monkeypatch.kaval.kotlintest.beInvalidWithAllReasons
import io.monkeypatch.kaval.kotlintest.beInvalidWithAny
import io.monkeypatch.kaval.kotlintest.beInvalidWithReason
import io.monkeypatch.kaval.kotlintest.beValid

class CollectionsSpec : DescribeSpec() {

    init {

        describe("basic") {

            describe("empty") {
                val validator: Validator<List<Int>> = empty()

                it("empty should accept an empty list") {
                    val result = validator.validate(emptyList())
                    result should beValid()
                }
                it("empty should accept a non-empty list") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beInvalidWithReason("require an empty collection, got {1, 2, 3}")
                }
            }

            describe("notEmpty") {
                val validator: Validator<List<Int>> = notEmpty()

                it("notEmpty should reject an notEmpty list") {
                    val result = validator.validate(emptyList())
                    result should beInvalidWithReason("require a not empty collection")
                }
                it("notEmpty should accept a non-notEmpty list") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beValid()
                }
            }

            describe("hasSize") {
                val validator: Validator<List<Int>> = hasSize(3)

                it("hasSize should reject a list with invalid size") {
                    val result = validator.validate(emptyList())
                    result should beInvalidWithReason("require to be equals to 3, got 0")
                }
                it("hasSize should accept a well-sized list") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beValid()
                }
            }

            describe("maxSize") {
                val validator: Validator<List<Int>> = maxSize(3)

                it("maxSize should accept a smaller list") {
                    val result = validator.validate(emptyList())
                    result should beValid()
                }
                it("maxSize should accept a well-sized list") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beValid()
                }
                it("maxSize should reject a bigger list") {
                    val result = validator.validate(listOf(1, 2, 3, 4, 5))
                    result should beInvalidWithReason("require to be lower or equals to 3, got 5")
                }
            }

            describe("minSize") {
                val validator: Validator<List<Int>> = minSize(3)

                it("minSize should reject a smaller list") {
                    val result = validator.validate(emptyList())
                    result should beInvalidWithReason("require to be greater or equals to 3, got 0")
                }
                it("minSize should accept a well-sized list") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beValid()
                }
                it("minSize should accept a bigger list") {
                    val result = validator.validate(listOf(1, 2, 3, 4, 5))
                    result should beValid()
                }
            }
        }

        describe("Collection") {
            describe("allValid") {
                val validator: Validator<List<Int>> = allValid { greaterThan(9) }

                it("allValid should accept an empty list") {
                    val result = validator.validate(emptyList())
                    result should beValid()
                }

                it("allValid should accept a list with all element valid") {
                    val result = validator.validate(listOf(10, 11, 12))
                    result should beValid()
                }

                it("allValid should reject a list with at least an element invalid") {
                    val result = validator.validate(listOf(10, 4, 12))
                    result should beInvalidWithReason("require to be greater than 9, got 4")
                }

                it("allValid should reject a list with all elements invalid") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beInvalidWithAllReasons(
                        "require to be greater than 9, got 1",
                        "require to be greater than 9, got 2",
                        "require to be greater than 9, got 3"
                    )
                }
            }

            describe("atLeastOneValid") {
                val validator: Validator<List<Int>> = atLeastOneValid { greaterThan(9) }

                it("atLeastOneValid should reject an empty list") {
                    val result = validator.validate(emptyList())
                    result should beInvalidWithReason("require at least one element valid, got no elements")
                }

                it("atLeastOneValid should accept a list with all element valid") {
                    val result = validator.validate(listOf(10, 11, 12))
                    result should beValid()
                }

                it("atLeastOneValid should accept a list with at least an element invalid") {
                    val result = validator.validate(listOf(10, 4, 12))
                    result should beValid()
                }

                it("atLeastOneValid should reject a list with all elements invalid") {
                    val result = validator.validate(listOf(1, 2, 3))
                    result should beInvalidWithAny(
                        startWith("require at least one element valid, all elements are invalid")
                    )
                }
            }
        }

        describe("Map") {
            describe("allValuesValid") {
                val validator: Validator<Map<String, Int>> = allValuesValid { greaterThan(9) }

                it("allValid should accept an empty map") {
                    val result = validator.validate(emptyMap())
                    result should beValid()
                }

                it("allValid should accept a map with all values valid") {
                    val result = validator.validate(
                        mapOf(
                            "ten" to 10,
                            "eleven" to 11,
                            "twelve" to 12
                        )
                    )
                    result should beValid()
                }

                it("allValid should reject a map with at least a value invalid") {
                    val result = validator.validate(
                        mapOf(
                            "ten" to 10,
                            "four" to 4,
                            "twelve" to 12
                        )
                    )
                    result should beInvalidWithReason("require to be greater than 9, got 4")
                }

                it("allValid should reject a map with all values invalid") {
                    val result = validator.validate(
                        mapOf(
                            "one" to 1,
                            "two" to 2,
                            "three" to 3
                        )
                    )
                    result should beInvalidWithAllReasons(
                        "require to be greater than 9, got 1",
                        "require to be greater than 9, got 2",
                        "require to be greater than 9, got 3"
                    )
                }
            }

            describe("allKeysValid") {
                val validator: Validator<Map<Int, String>> = allKeysValid { greaterThan(9) }

                it("allValid should accept an empty map") {
                    val result = validator.validate(emptyMap())
                    result should beValid()
                }

                it("allValid should accept a map with all keys valid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            11 to "eleven",
                            12 to "twelve"
                        )
                    )
                    result should beValid()
                }

                it("allValid should reject a map with at least a key invalid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            4 to "four",
                            12 to "twelve"
                        )
                    )
                    result should beInvalidWithReason("require to be greater than 9, got 4")
                }

                it("allValid should reject a map with all keys invalid") {
                    val result = validator.validate(
                        mapOf(
                            1 to "one",
                            2 to "two",
                            3 to "three"
                        )
                    )
                    result should beInvalidWithAllReasons(
                        "require to be greater than 9, got 1",
                        "require to be greater than 9, got 2",
                        "require to be greater than 9, got 3"
                    )
                }
            }

            describe("allEntriesValid") {
                val validator: Validator<Map<Int, String>> = allEntriesValid {
                    entryValidator(greaterThan(9), notBlank)
                }

                it("allValid should accept an empty map") {
                    val result = validator.validate(emptyMap())
                    result should beValid()
                }

                it("allValid should accept a map with all entries valid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            11 to "eleven",
                            12 to "twelve"
                        )
                    )
                    result should beValid()
                }

                it("allValid should reject a map with at least an entry invalid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            4 to "four",
                            12 to "twelve"
                        )
                    )
                    result should beInvalidWithReason(
                        "require to be greater than 9, got 4"
                    )
                }

                it("allValid should reject a map with all entries invalid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "",
                            2 to "two",
                            3 to ""
                        )
                    )
                    result should beInvalidWithAllReasons(
                        "require to be not blank",
                        "require to be greater than 9, got 2",
                        "require to be greater than 9, got 3"
                    )
                }
            }

            describe("atLeastOneValueValid") {
                val validator: Validator<Map<String, Int>> = atLeastOneValueValid { greaterThan(9) }

                it("atLeastOneValid should reject an empty map") {
                    val result = validator.validate(emptyMap())
                    result should beInvalidWithReason("require at least one value valid, got no values")
                }

                it("atLeastOneValid should accept a map with all value valid") {
                    val result = validator.validate(
                        mapOf(
                            "ten" to 10,
                            "eleven" to 11,
                            "twelve" to 12
                        )
                    )
                    result should beValid()
                }

                it("atLeastOneValid should accept a map with at least a value invalid") {
                    val result = validator.validate(
                        mapOf(
                            "ten" to 10,
                            "four" to 4,
                            "twelve" to 12
                        )
                    )
                    result should beValid()
                }

                it("atLeastOneValid should reject a map with all value invalid") {
                    val result = validator.validate(
                        mapOf(
                            "one" to 1,
                            "two" to 2,
                            "three" to 3
                        )
                    )
                    result should beInvalidWithAny(
                        startWith("require at least one value valid, all values are invalid")
                    )
                }
            }

            describe("atLeastOneKeyValid") {
                val validator: Validator<Map<Int, String>> = atLeastOneKeyValid { greaterThan(9) }

                it("atLeastOneValid should reject an empty map") {
                    val result = validator.validate(emptyMap())
                    result should beInvalidWithReason("require at least one key valid, got no keys")
                }

                it("atLeastOneValid should accept a map with all key valid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            11 to "eleven",
                            12 to "twelve"
                        )
                    )
                    result should beValid()
                }

                it("atLeastOneValid should accept a map with at least a key invalid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            4 to "four",
                            12 to "twelve"
                        )
                    )
                    result should beValid()
                }

                it("atLeastOneValid should reject a map with all key invalid") {
                    val result = validator.validate(
                        mapOf(
                            1 to "one",
                            2 to "two",
                            3 to "three"
                        )
                    )
                    result should beInvalidWithAny(
                        startWith("require at least one key valid, all keys are invalid")
                    )
                }
            }

            describe("atLeastOneEntryValid") {
                val validator: Validator<Map<Int, String>> = atLeastOneEntryValid {
                    entryValidator(greaterThan(9), notBlank)
                }

                it("atLeastOneValid should reject an empty map") {
                    val result = validator.validate(emptyMap())
                    result should beInvalidWithReason("require at least one entry valid, got no entries")
                }

                it("atLeastOneValid should accept a map with all entry valid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            11 to "eleven",
                            12 to "twelve"
                        )
                    )
                    result should beValid()
                }

                it("atLeastOneValid should accept a map with at least a entry invalid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "ten",
                            4 to "four",
                            12 to "twelve"
                        )
                    )
                    result should beValid()
                }

                it("atLeastOneValid should reject a map with all entry invalid") {
                    val result = validator.validate(
                        mapOf(
                            10 to "",
                            2 to "two",
                            3 to ""
                        )
                    )
                    result should beInvalidWithAny(
                        startWith("require at least one entry valid, all entries are invalid")
                    )
                }
            }
        }
    }
}
