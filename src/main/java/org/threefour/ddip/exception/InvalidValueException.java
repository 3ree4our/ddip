package org.threefour.ddip.exception;

public abstract class InvalidValueException extends IllegalArgumentException {
    public InvalidValueException(String message) {
        super(message);
    }
}
