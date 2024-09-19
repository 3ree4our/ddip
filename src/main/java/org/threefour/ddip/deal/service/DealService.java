package org.threefour.ddip.deal.service;

import org.threefour.ddip.deal.domain.InitializeDealRequest;

public interface DealService {
    int createDeal(Long buyerId, InitializeDealRequest initializeDealRequest);

    int getWinningNumber(Long productId, Long memberId);
}
