package org.threefour.ddip.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.InitializeDealRequest;
import org.threefour.ddip.deal.exception.DealNotFoundException;
import org.threefour.ddip.deal.repository.DealRepository;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.FormatConverter;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.threefour.ddip.deal.exception.ExceptionMessage.DEAL_PRODUCT_BUYER_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 20)
public class DealServiceImpl implements DealService {
  private final ProductService productService;
  private final DealRepository dealRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional(isolation = REPEATABLE_READ, timeout = 10)
  public int createDeal(Long buyerId, InitializeDealRequest initializeDealRequest) {
    Product product
            = productService.getProduct(FormatConverter.parseToLong(initializeDealRequest.getProductId()), false);
    Member seller
            = memberRepository.findById(FormatConverter.parseToLong(initializeDealRequest.getSellerId())).get();
    Member buyer = memberRepository.findById(buyerId).get();
    int waitingNumber = dealRepository.countByProductAndSellerAndDeleteYnFalse(product, seller) + 1;

    dealRepository.save(Deal.from(initializeDealRequest, product, seller, buyer, waitingNumber));

    return waitingNumber;
  }

  @Override
  public int getWinningNumber(Long productId, Long memberId) {
    try {
      return getDealByProductIdAndBuyerId(productId, memberId).getWaitingNumber();
    } catch (DealNotFoundException dnfe) {
      return -1;
    }
  }

  private Deal getDealByProductIdAndBuyerId(Long productId, Long buyerId) {
    return dealRepository.findByProductIdAndBuyerIdAndDeleteYnFalse(productId, buyerId)
            .orElseThrow(
                    () -> new DealNotFoundException(
                            String.format(DEAL_PRODUCT_BUYER_NOT_FOUND_EXCEPTION_MESSAGE, productId, buyerId)
                    )
            );
  }

  @Override
  public int getWaitingCount(Long productId) {
    return dealRepository.countByProductIdAndDeleteYnFalse(productId);
  }
}
