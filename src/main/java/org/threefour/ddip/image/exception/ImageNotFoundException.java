package org.threefour.ddip.image.exception;

import javax.persistence.EntityNotFoundException;

public class ImageNotFoundException extends EntityNotFoundException {
    public ImageNotFoundException(String message) {
        super(message);
    }
}
