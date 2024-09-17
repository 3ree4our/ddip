package org.threefour.ddip.product.priceinformation.exception;

import org.threefour.ddip.exception.InvalidValueException;

public class InvalidNextDiscountedAtException extends InvalidValueException {
    public InvalidNextDiscountedAtException(String message) {
        super(message);
    }
}
