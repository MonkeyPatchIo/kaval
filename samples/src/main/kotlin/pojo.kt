import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.nullOr
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan
import io.monkeypatch.kaval.core.validator.Strings.maxLength
import io.monkeypatch.kaval.core.validator.Strings.notBlank
import io.monkeypatch.kaval.reflect.reflectValidator

data class User(
    val firstName: String,
    val lastName: String,
    val address: Address?
) {
    companion object {
        /**
         * A [User] is valid when:
         * - firstName is not blank and length <= 128
         * - lastName is not blank and length <= 255
         * - address is valid if not null
         */
        val validator: Validator<User> =
            reflectValidator {
                User::firstName { notBlank and maxLength(128) }
                User::lastName { notBlank and maxLength(255) }
                User::address { nullOr { Address.validator } }
            }
    }
}

data class Address(
    val line1: String,
    val line2: String,
    val zipCode: Int,
    val city: String
) {
    companion object {
        /**
         * An [Address] is valid when:
         * - the line1 is not blank and length <= 255
         * - the line2 length <= 255
         * - the zipCode is > 0
         * - the city is not blank
         */
        val validator: Validator<Address> =
            reflectValidator {
                Address::line1 { notBlank and maxLength(255) }
                Address::line2 { maxLength(255) }
                Address::zipCode { greaterThan(0) }
                Address::city { notBlank }
            }
    }
}
