import io.monkeypatch.kaval.arrow.KavalEither.check

fun main() {
    val addressOk = Address(
        line1 = "42 avenue Monoid",
        line2 = "Semigroup block",
        zipCode = 42000,
        city = "LambdaCity"
    )

    val addressKo = Address(
        line1 = " ".repeat(500),
        line2 = "",
        zipCode = -1,
        city = ""
    )

    println(addressOk.check(Address.validator))
    // Right(Address(line1=42 avenue Monoid, line2=Semigroup block, zipCode=42000, city=LambdaCity))

    println(addressKo.check(Address.validator))
    // Left(InvalidException(invalid=Invalid:
    //  - [line1] requires to be not blank
    //  - [line1.length] requires to be lower or equals to 255, got 500
    //  - [zipCode] requires to be greater than 0, got -1
    //  - [city] requires to be not blank))
}
