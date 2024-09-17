package org.threefour.ddip.product.priceinformation.domain;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class GetPriceInformationResponse {
    private short priceDiscountPeriod;
    private byte priceDiscountRate;
    private Timestamp nextDiscountedAt;
    private int minPrice;

    private GetPriceInformationResponse(
            short priceDiscountPeriod, byte priceDiscountRate, Timestamp nextDiscountedAt, int minPrice
    ) {
        this.priceDiscountPeriod = priceDiscountPeriod;
        this.priceDiscountRate = priceDiscountRate;
        this.nextDiscountedAt = nextDiscountedAt;
        this.minPrice = minPrice;
    }

    public static GetPriceInformationResponse from(PriceInformation priceInformation) {
        return new GetPriceInformationResponse(
                priceInformation.getPriceDiscountPeriod().getValue(),
                priceInformation.getPriceDiscountRate().getPercentValue(),
                priceInformation.getNextDiscountedAt().getValue(),
                priceInformation.getMinPrice().getValue()
        );
    }
}
