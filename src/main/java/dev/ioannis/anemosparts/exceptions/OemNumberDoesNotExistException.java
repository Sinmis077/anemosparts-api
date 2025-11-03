package dev.ioannis.anemosparts.exceptions;

public class OemNumberDoesNotExistException extends RuntimeException {
    public OemNumberDoesNotExistException(String message)
    {
        super(message.isEmpty() ? "OemNumber doesn't exist" : message);
    }
}
