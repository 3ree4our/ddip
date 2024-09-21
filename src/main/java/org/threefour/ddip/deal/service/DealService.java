package org.threefour.ddip.deal.service;

import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.DealStatus;
import org.threefour.ddip.deal.domain.InitializeDealRequest;

import java.util.List;

public interface DealService {

  int createDeal(Long buyerId, InitializeDealRequest initializeDealRequest);

  int getWaitingNumber(Long productId, Long memberId);

  int getWaitingNumberCount(Long productId);

  Deal checkWaitingStatus(Long productId, Long buyerId);

  List<Long> getProductIdsByUserId(Long id);

  void completeDeal(Long productId);

  void cancelDeal(Long productId);
}
