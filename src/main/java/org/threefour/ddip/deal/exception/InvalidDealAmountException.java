package org.threefour.ddip.deal.exception;

import org.threefour.ddip.exception.InvalidValueException;

public class InvalidDealAmountException extends InvalidValueException {
    public InvalidDealAmountException(String message) {
        super(message);
    }
}
