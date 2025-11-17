package dev.ioannis.anemosparts.annotations.validators;

import dev.ioannis.anemosparts.annotations.Image;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file.getContentType() != null && file.getContentType().startsWith("image/");
    }
}
