package org.threefour.ddip.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.threefour.ddip.util.EntityConstant.DATE_PATTERN;
import static org.threefour.ddip.util.RegularExpressionConstant.POSITIVE_INTEGER_OR_ZERO_PATTERN;
import static org.threefour.ddip.util.RegularExpressionConstant.POSITIVE_INTEGER_PATTERN;

public class FormatValidator {
    public static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean hasValue(Object value) {
        return value != null;
    }

    public static boolean hasValue(List value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isPositiveNumberPattern(String value) {
        return value.matches(POSITIVE_INTEGER_PATTERN);
    }

    public static boolean isPositiveNumberOrZeroPattern(String value) {
        return value.matches(POSITIVE_INTEGER_OR_ZERO_PATTERN);
    }

    public static boolean isDatePattern(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
