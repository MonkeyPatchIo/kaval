import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.monkeypatch.kaval.kotlintest.beInvalid
import io.monkeypatch.kaval.kotlintest.beValid

class PojoSpec : DescribeSpec() {

    init {

        describe("User.validator") {

            it("should accept a valid user") {
                val user = User(
                    firstName = "Molly",
                    lastName = "Millions",
                    address = Address(
                        line1 = "1337 plop street",
                        line2 = "",
                        zipCode = 42000,
                        city = "Chiba"
                    )
                )
                val result = User.validator(user)
                result should beValid()
            }

            it("should accept an invalid user") {
                val user = User(
                    firstName = "",
                    lastName = "x".repeat(500),
                    address = Address(
                        line1 = "",
                        line2 = "",
                        zipCode = -1,
                        city = ""
                    )
                )
                val result = User.validator(user)
                result should beInvalid()
            }
        }
    }
}
