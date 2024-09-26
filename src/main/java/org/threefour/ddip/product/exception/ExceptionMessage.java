package org.threefour.ddip.product.exception;

public class ExceptionMessage {
    public static final String PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE
            = "해당하는 상품을 찾을 수 없습니다. 다시 시도해 주세요. 전송된 ID: %d";

    public static final String PRICE_NO_VALUE_EXCEPTION_MESSAGE = "가격은 필수값입니다.";
    public static final String INVALID_PRICE_EXCEPTION_MESSAGE = "가격은 양의 정수(1 이상의 숫자)여야 합니다. 전송된 금액: %s";

    public static final String UPDATE_FORM_NO_VALUE_EXCEPTION_MESSAGE
            = "수정값이 전송되지 않았습니다. 수정값을 입력 후 다시 시도해 주세요.";
}
