package org.threefour.ddip.product.exception;

import org.threefour.ddip.exception.NoValueException;

public class PriceNoValueException extends NoValueException {
    public PriceNoValueException(String message) {
        super(message);
    }
}
