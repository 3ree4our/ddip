package org.threefour.ddip.deal.exception;

public class ExceptionMessage {
    public static final String DEAL_PRODUCT_BUYER_NOT_FOUND_EXCEPTION_MESSAGE
            = "해당하는 거래를 찾을 수 없습니다. 다시 시도해 주세요. 전송된 상품 ID: %s, 구매자 ID: %s";

    public static final String DEAL_AMOUNT_NO_VALUE_EXCEPTION_MESSAGE = "거래 금액은 필수값입니다.";
    public static final String INVALID_DEAL_AMOUNT_EXCEPTION_MESSAGE
            = "거래 금액은 양의 정수(1 이상의 숫자)여야 합니다. 전송된 금액: %s";
}
