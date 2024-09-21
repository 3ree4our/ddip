package org.threefour.ddip.deal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class InitializeDealRequest {
    private String productId;
    private String sellerId;
    private String dealAmount;
}
