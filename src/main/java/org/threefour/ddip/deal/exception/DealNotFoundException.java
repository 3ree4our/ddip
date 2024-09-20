package org.threefour.ddip.deal.exception;

import javax.persistence.EntityNotFoundException;

public class DealNotFoundException extends EntityNotFoundException {
    public DealNotFoundException(String message) {
        super(message);
    }
}