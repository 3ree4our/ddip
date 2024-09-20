package org.threefour.ddip.deal.service;

import org.threefour.ddip.deal.domain.InitializeDealRequest;

public interface DealService {
    int createDeal(Long buyerId, InitializeDealRequest initializeDealRequest);

    int getWaitingNumber(Long productId, Long memberId);

    int getWaitingNumberCount(Long productId);
}
