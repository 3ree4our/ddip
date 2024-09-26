package org.threefour.ddip.deal.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DealStatus {
    BEFORE_DEAL("거래 전"),
    IN_PROGRESS("거래 중"),
    CANCELED("거래 취소"),
    BEFORE_PAYMENT("결제 전"),
    PAID("결제 완료"),
    SCORED("평가 완료"),
    REFUNDED("환불 완료");

    private final String description;
}
