package org.threefour.ddip.deal.service;

import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.DealStatus;
import org.threefour.ddip.deal.domain.InitializeDealRequest;

import java.util.List;

public interface DealService {

  int createDeal(Long buyerId, InitializeDealRequest initializeDealRequest);

  int getWaitingNumber(Long productId, Long memberId);

  int getWaitingNumberCount(Long productId);

  DealStatus checkWaitingStatus(Long productId, Long buyerId);

  List<Long> getProductIdsForUserChats(Long id);

  void completeDeal(Long productId);

  void cancelDeal(Long productId);

  DealStatus getProductIdAndDealStatusAndDeleteYnFalse(Long productId, DealStatus dealStatus);

}
