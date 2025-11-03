package dev.ioannis.anemosparts.exceptions;

public class NullModelsFoundException extends RuntimeException {
    public NullModelsFoundException(String message) {
        super(message.isEmpty() ? "Found where there should be models" : message);
    }
}
