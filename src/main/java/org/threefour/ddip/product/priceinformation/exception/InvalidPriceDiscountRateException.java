package org.threefour.ddip.product.priceinformation.exception;

import org.threefour.ddip.exception.InvalidValueException;

public class InvalidPriceDiscountRateException extends InvalidValueException {
    public InvalidPriceDiscountRateException(String message) {
        super(message);
    }
}
