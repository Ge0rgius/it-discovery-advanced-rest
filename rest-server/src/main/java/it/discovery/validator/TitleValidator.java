package it.discovery.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<TitleConstraint, String> {

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (title == null || title.isEmpty()) {
            return false;
        }
        char firstCh = title.charAt(0);
        return Character.isUpperCase(firstCh);
    }
}
