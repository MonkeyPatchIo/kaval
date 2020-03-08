# Kaval reflect

Contains validator based on reflection

## Provided Validator

- `property(property: KProperty<C>, childValidator: () -> Validator<C>)`: validate a property,
same as `field` validator but we does not need to provide the field name.
