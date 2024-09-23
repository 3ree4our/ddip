package org.threefour.ddip.util;

import org.springframework.data.domain.Sort;
import org.threefour.ddip.exception.*;
import org.threefour.ddip.image.domain.TargetType;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.threefour.ddip.exception.ExceptionMessage.*;
import static org.threefour.ddip.util.EntityConstant.DATE_PATTERN;

public class FormatConverter {
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public static long parseToLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException nfe) {
            throw new ParsingLongException((String.format(PARSING_LONG_EXCEPTION_MESSAGE, number)));
        }
    }

    public static int parseToInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            throw new ParsingIntegerException((String.format(PARSING_INTEGER_EXCEPTION_MESSAGE, number)));
        }
    }

    public static short parseToShort(String number) throws ParsingShortException {
        try {
            return Short.parseShort(number);
        } catch (NumberFormatException nfe) {
            throw new ParsingShortException((String.format(PARSING_SHORT_EXCEPTION_MESSAGE, number)));
        }
    }

    public static float parseToFloat(String primeNumber) {
        try {
            return Float.parseFloat(primeNumber);
        } catch (NumberFormatException nfe) {
            throw new ParsingFloatException((String.format(PARSING_FLOAT_EXCEPTION_MESSAGE, primeNumber)));
        }
    }

    public static Timestamp parseToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(false);
        try {
            return new Timestamp(dateFormat.parse(date).getTime());
        } catch (ParseException e) {
            throw new ParsingTimestampException(String.format(PARSING_TIMESTAMP_EXCEPTION_MESSAGE, date));
        }
    }

    public static boolean parseToBoolean(String value) {
        if (!value.equals(TRUE) && !value.equals(FALSE)) {
            throw new ParsingBooleanException((String.format(PARSING_BOOLEAN_EXCEPTION_MESSAGE, value)));
        }

        return Boolean.parseBoolean(value);
    }

    public static TargetType parseToTargetType(String targetType) {
        try {
            return TargetType.valueOf(targetType);
        } catch (IllegalArgumentException iae) {
            throw new InvalidTargetTypeException(String.format(INVALID_TARGET_TYPE_EXCEPTION_MESSAGE, targetType));
        }
    }

    public static Sort parseSortString(String sort) {
        String[] parts = sort.split(",");
        return Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
    }
}
