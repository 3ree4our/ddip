package org.threefour.ddip.deal.exception;

import org.threefour.ddip.exception.NoValueException;

public class IdenticalSellerException extends NoValueException {
    public IdenticalSellerException(String message) {
        super(message);
    }
}
