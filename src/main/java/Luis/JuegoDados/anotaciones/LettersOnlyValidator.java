package Luis.JuegoDados.anotaciones;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LettersOnlyValidator implements ConstraintValidator<LettersOnly, String> {

    @Override
    public void initialize(LettersOnly constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[a-zA-Z]*$");
    }

}
