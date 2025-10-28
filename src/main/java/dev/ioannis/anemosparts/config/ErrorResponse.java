package dev.ioannis.anemosparts.config;

import java.util.List;

public record ErrorResponse(Boolean success, String message, List<FieldError> errors) {
}
