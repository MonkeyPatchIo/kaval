import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.validator.Collections.allValid
import io.monkeypatch.kaval.core.validator.Comparables.greaterThan

/**
 * Validate a list of int, where all int should be > 9
 */
val listValidator: Validator<List<Int>> = allValid { greaterThan(9) }
