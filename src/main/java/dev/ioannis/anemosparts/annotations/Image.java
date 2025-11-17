package dev.ioannis.anemosparts.annotations;

import dev.ioannis.anemosparts.annotations.validators.ImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Image {
    String message() default "The provided file is not an image";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
