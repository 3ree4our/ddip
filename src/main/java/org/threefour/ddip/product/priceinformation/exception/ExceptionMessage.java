package org.threefour.ddip.product.priceinformation.exception;

public class ExceptionMessage {
    public static final String PRICE_DISCOUNT_PERIOD_NO_VALUE_EXCEPTION_MESSAGE = "가격 할인 주기가 전송되지 않았습니다.";
    public static final String INVALID_PRICE_DISCOUNT_PERIOD_EXCEPTION_MESSAGE
            = "가격 할인 주기는 양의 정수(1 이상의 숫자)여야 합니다. 전송된 가격 할인 주기: %s";

    public static final String PRICE_DISCOUNT_RATE_NO_VALUE_EXCEPTION_MESSAGE = "가격 할인율이 전송되지 않았습니다.";
    public static final String INVALID_PRICE_DISCOUNT_RATE_EXCEPTION_MESSAGE
            = "가격 할인율(%)은 양의 정수(1 이상의 숫자)여야 합니다. 전송된 가격 할인율: %s";

    public static final String NEXT_DISCOUNTED_AT_NO_VALUE_EXCEPTION_MESSAGE = "다음 할인 날짜가 전송되지 않았습니다.";
    public static final String INVALID_NEXT_DISCOUNTED_AT_EXCEPTION_MESSAGE
            = "다음 할인 날짜는 날짜 형식이어야 합니다. 전송된 가격 할인율: %s";

    public static final String MIN_PRICE_NO_VALUE_EXCEPTION_MESSAGE = "최소가격이 전송되지 않았습니다.";
    public static final String INVALID_MIN_PRICE_EXCEPTION_MESSAGE
            = "최소가격은 양의 정수(1 이상의 숫자)여야 합니다. 전송된 금액: %s";
}
