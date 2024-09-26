package org.threefour.ddip.exception;

public abstract class NoValueException extends IllegalArgumentException {
    public NoValueException(String message) {
        super(message);
    }
}
