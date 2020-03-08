import io.monkeypatch.kaval.core.Validator
import io.monkeypatch.kaval.core.nullOr
import io.monkeypatch.kaval.core.validator.Strings.matches

/**
 * A validator that accept null, or a String matching [Regex] `pl.*p`
 */
val plopValidator: Validator<String?> = nullOr {
    matches(Regex("pl.*p"))
}
