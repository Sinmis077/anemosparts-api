package dev.ioannis.anemosparts.exceptions;

public class EntityModelsNotFoundException extends RuntimeException {
    public EntityModelsNotFoundException(String message) {
        super(message.isEmpty() ? "Found where there should be models" : message);
    }
}
