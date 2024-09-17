package org.threefour.ddip.util;

import java.util.List;

import static org.threefour.ddip.util.RegularExpressionConstant.POSITIVE_INTEGER_PATTERN;

public class FormatValidator {
    public static boolean isNoValue(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isNoValue(List value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNumberPattern(String value) {
        return value.matches(POSITIVE_INTEGER_PATTERN);
    }
}
