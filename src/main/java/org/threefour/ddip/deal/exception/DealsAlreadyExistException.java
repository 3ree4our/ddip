package org.threefour.ddip.deal.exception;

import javax.persistence.EntityNotFoundException;

public class DealsAlreadyExistException extends EntityNotFoundException {
    public DealsAlreadyExistException(String message) {
        super(message);
    }
}