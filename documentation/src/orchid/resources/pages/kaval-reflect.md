---
---

# Kaval reflect

Contains validator based on reflection

## Provided Validator

- `property(property: KProperty<C>, childValidator: () -> Validator<C>)`: validate a property,
same as `field` validator but we does not need to provide the field name.

## DSL to build property-based validator

The `reflectValidator` function create a validator based on properties

```kotlin
// A simple POJO
data class Address(
    val line1: String,
    val line2: String,
    val zipCode: Int,
    val city: String
)

// And the validator
val validator: Validator<Address> =
    reflectValidator {
        Address::line1 { notBlank and maxLength(255) }
        Address::line2 { maxLength(255) }
        Address::zipCode { greaterThan(0) }
        Address::city { notBlank }
    }
```
