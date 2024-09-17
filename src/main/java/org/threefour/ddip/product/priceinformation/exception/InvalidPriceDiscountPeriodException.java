package org.threefour.ddip.product.priceinformation.exception;

import org.threefour.ddip.exception.InvalidValueException;

public class InvalidPriceDiscountPeriodException extends InvalidValueException {
    public InvalidPriceDiscountPeriodException(String message) {
        super(message);
    }
}
