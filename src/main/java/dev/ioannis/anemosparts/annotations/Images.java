package dev.ioannis.anemosparts.annotations;

import dev.ioannis.anemosparts.annotations.validators.ImagesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImagesValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Images {
    String message() default "The provided files are not images";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
