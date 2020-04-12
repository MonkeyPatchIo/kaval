---
---

# Samples

You can retrieve all these samples into the [Github repository](https://github.com/MonkeyPatchIo/kaval/tree/master/samples/src/main/kotlin)

## Validate a POJO

Let's write a validator for each POJO, here we use the companion object to declare the validator.

```kotlin
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
```

## Validate a nullable object

To validate an object only when it's not null, use the `io.monkeypatch.kaval.core.nullOr`

```kotlin
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.nullOr
import io.monkeypatch.kaval.core.validator.Strings.matches

/**
 * A validator that accept null, or a String matching [Regex] `pl.*p`
 */
val plopValidator: Validator<String?> = nullOr {
    matches(Regex("pl.*p"))
}
```

## Validate a collection

You can retrieve some useful collection validators [here](./kaval-core#collections)

```kotlin
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Collections.allValid
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan

/**
 * Validate a list of int, where all int should be > 9
 */
val listValidator: Validator<List<Int>> = allValid { greaterThan(9) }
```

### Validate a sealed class

You can combine validators that matching sub-classes with `io.monkeypatch.kaval.core.whenIsInstance`

```kotlin
import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.field
import io.monkeypatch.kaval.core.validator.Ints.inRange
import io.monkeypatch.kaval.core.whenIsInstance

/**
 * Let's define a sealed hierarchy for HttpStatus
 */
sealed class HttpStatus {

    data class Ok(val statusCode: Int) : HttpStatus()

    data class Error(val statusCode: Int) : HttpStatus()

    data class ServerError(val statusCode: Int) : HttpStatus()

    companion object {

        /**
         * Validate the status statusCode for each HttpStatus variant
         */
        val validator: Validator<HttpStatus> = whenIsInstance<HttpStatus, Ok> {
            // Only check when is an instance of HttpStatus.Ok
            field("statusCode", Ok::statusCode) { inRange(200..299) }
        } and whenIsInstance {
            // Only check when is an instance of HttpStatus.Error
            field("statusCode", Error::statusCode) { inRange(400..499) }
        } and whenIsInstance {
            // Only check when is an instance of HttpStatus.ServerError
            field("statusCode", ServerError::statusCode) { inRange(500..599) }
        }
    }
}
```
