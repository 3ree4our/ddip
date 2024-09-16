package org.threefour.ddip.util;

import org.threefour.ddip.exception.InvalidTargetTypeException;
import org.threefour.ddip.exception.ParsingLongException;
import org.threefour.ddip.image.domain.TargetType;

import static org.threefour.ddip.exception.ExceptionMessage.INVALID_TARGET_TYPE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.exception.ExceptionMessage.PARSING_LONG_EXCEPTION_MESSAGE;

public class FormatConverter {

    public static long parseToLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException nfe) {
            throw new ParsingLongException((String.format(PARSING_LONG_EXCEPTION_MESSAGE, number)));
        }
    }

    public static TargetType parseToTargetType(String targetType) {
        try {
            return TargetType.valueOf(targetType);
        } catch (IllegalArgumentException iae) {
            throw new InvalidTargetTypeException(String.format(INVALID_TARGET_TYPE_EXCEPTION_MESSAGE, targetType));
        }
    }
}
