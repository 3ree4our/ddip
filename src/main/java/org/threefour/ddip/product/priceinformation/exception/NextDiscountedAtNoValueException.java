package org.threefour.ddip.product.priceinformation.exception;

import org.threefour.ddip.exception.NoValueException;

public class NextDiscountedAtNoValueException extends NoValueException {
    public NextDiscountedAtNoValueException(String message) {
        super(message);
    }
}
