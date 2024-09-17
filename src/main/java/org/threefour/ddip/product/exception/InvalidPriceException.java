package org.threefour.ddip.product.exception;

import org.threefour.ddip.exception.InvalidValueException;

public class InvalidPriceException extends InvalidValueException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
