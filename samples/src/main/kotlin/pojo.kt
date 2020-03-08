import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.nullOr
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan
import io.monkeypatch.kaval.core.validator.Strings.maxLength
import io.monkeypatch.kaval.core.validator.Strings.notBlank
import io.monkeypatch.kaval.reflect.property

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
            property<User, String>(User::firstName) {
                notBlank and maxLength(128)
            } and property(User::lastName) {
                notBlank and maxLength(255)
            } and property(User::address) {
                nullOr { Address.validator }
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
            property<Address, String>(Address::line1) {
                notBlank and maxLength(255)
            } and property(Address::line2) {
                maxLength(255)
            } and property(Address::zipCode) {
                greaterThan(0)
            } and property(Address::city) {
                notBlank
            }
    }
}
