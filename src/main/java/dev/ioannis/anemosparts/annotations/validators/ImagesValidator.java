package dev.ioannis.anemosparts.annotations.validators;

import dev.ioannis.anemosparts.annotations.Images;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImagesValidator implements ConstraintValidator<Images, List<MultipartFile>> {
    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        for (MultipartFile file : files) {
            // BAD HABIT: Redundant null check - "file.getContentType() != null &&" is unnecessary after already checking "== null"
            // Should simplify to: if(file.getContentType() == null || !file.getContentType().startsWith("image/"))
            if(file.getContentType() == null || (file.getContentType() != null && !file.getContentType().startsWith("image/"))) return false;
        }

        return true;
    }
}
