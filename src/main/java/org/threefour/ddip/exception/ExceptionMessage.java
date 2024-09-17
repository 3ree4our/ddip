package org.threefour.ddip.exception;

public class ExceptionMessage {
    public static final String PARSING_LONG_EXCEPTION_MESSAGE
            = "숫자값만 Long 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String PARSING_SHORT_EXCEPTION_MESSAGE
            = "숫자값만 Short 타입으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
    public static final String INVALID_TARGET_TYPE_EXCEPTION_MESSAGE
            = "존재하는 도메인만 TargetType으로 변환할 수 있습니다. 현재 변환 대상 값: %s";
}
