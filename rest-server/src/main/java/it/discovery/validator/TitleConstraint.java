package it.discovery.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = TitleValidator.class)
public @interface TitleConstraint {

    String message() default "Title should start with capital letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
