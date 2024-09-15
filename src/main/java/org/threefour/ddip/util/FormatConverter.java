package org.threefour.ddip.util;

import org.threefour.ddip.exception.InvalidTargetTypeException;
import org.threefour.ddip.image.domain.TargetType;

import static org.threefour.ddip.exception.ExceptionMessage.INVALID_TARGET_TYPE_EXCEPTION_MESSAGE;

public class FormatConverter {
    public static TargetType parseToTargetType(String targetType) {
        try {
            return TargetType.valueOf(targetType);
        } catch (IllegalArgumentException iae) {
            throw new InvalidTargetTypeException(String.format(INVALID_TARGET_TYPE_EXCEPTION_MESSAGE, targetType));
        }
    }
}
