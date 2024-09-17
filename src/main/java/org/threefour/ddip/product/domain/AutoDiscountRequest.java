package org.threefour.ddip.product.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AutoDiscountRequest {
    private String firstDiscountDate;
    private String priceDiscountPeriod;
    private String priceDiscountRate;
    private String minPrice;
}
