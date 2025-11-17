package dev.ioannis.anemosparts.exceptions;

public class NullBrandException extends RuntimeException {
    public NullBrandException(String message) {
        super(message.isEmpty() ? "Brand doesn't exist" : message);
    }
}
