package org.threefour.ddip.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.domain.ChatMessage;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.repository.ChatRepository;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.DealStatus;
import org.threefour.ddip.deal.domain.InitializeDealRequest;
import org.threefour.ddip.deal.exception.DealNotFoundException;
import org.threefour.ddip.deal.repository.DealRepository;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.FormatConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.threefour.ddip.deal.exception.ExceptionMessage.DEAL_PRODUCT_BUYER_NOT_FOUND_EXCEPTION_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 20)
public class DealServiceImpl implements DealService {
  private final ProductService productService;
  private final DealRepository dealRepository;
  private final MemberRepository memberRepository;
  private final ChatRepository chatRepository;
  private final SimpMessagingTemplate messagingTemplate;

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
  public int getWaitingNumberCount(Long productId) {
    return dealRepository.countByProductIdAndDealStatusNotAndDeleteYnFalse(productId, DealStatus.CANCELED);
  }

  @Override
  public int getWaitingNumber(Long productId, Long memberId) {
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
  @Transactional(isolation = REPEATABLE_READ, timeout = 10)
  public DealStatus checkWaitingStatus(Long productId, Long buyerId) {
    Optional<Deal> dealOptByBuyer = dealRepository.findByProductIdAndBuyerIdAndDeleteYnFalse(productId, buyerId);
    Optional<Deal> dealOptByStatus = dealRepository.findByProductIdAndDealStatusAndDeleteYnFalse(productId, DealStatus.IN_PROGRESS);

    if (dealOptByBuyer.isPresent()) {
      Deal dealByBuyer = dealOptByBuyer.get();
      int waitingNumber = dealByBuyer.getWaitingNumber();

      if (dealOptByStatus.isPresent()) {
        Deal dealInProgress = dealOptByStatus.get();

        // 현재 진행 중인 거래가 해당 구매자의 거래인 경우
        if (dealInProgress.getBuyer().getId().equals(buyerId)) {
          return DealStatus.IN_PROGRESS;
        }

        // 현재 진행 중인 거래의 대기 번호가 구매자의 대기 번호보다 작은 경우
        if (dealInProgress.getWaitingNumber() < waitingNumber) {
          return dealByBuyer.getDealStatus(); // 여전히 대기 중
        }
      }

      // 구매자의 대기 번호가 1이고 아직 BEFORE_DEAL 상태인 경우
      if (waitingNumber == 1 && dealByBuyer.getDealStatus() == DealStatus.BEFORE_DEAL) {
        dealByBuyer.setDealStatus(DealStatus.IN_PROGRESS);
        dealRepository.save(dealByBuyer);
        return DealStatus.IN_PROGRESS;
      }

      return dealByBuyer.getDealStatus();
    }

    return null; // 해당 구매자의 거래가 없는 경우
  }

  @Override
  public List<Long> getProductIdsForUserChats(Long id) {
    return dealRepository.findProductIdsForUserChats(id);
  }

  @Override
  public DealStatus getProductIdAndDealStatusAndDeleteYnFalse(Long productId, DealStatus dealStatus) {
    Optional<Deal> deal = dealRepository.findByProductIdAndDealStatusAndDeleteYnFalse(productId, DealStatus.PAID);
    if (deal.isPresent()) return deal.get().getDealStatus();
    return DealStatus.IN_PROGRESS;
  }


  @Override
  @Transactional
  public void completeDeal(Long productId) {
    List<Deal> deals = dealRepository.findByProductIdAndAndDeleteYnFalse(productId);

    for (Deal deal : deals) {
      if (deal.getDealStatus().name().equals("IN_PROGRESS")) {
        deal.setDealStatus(DealStatus.PAID);
        dealRepository.save(deal);
      } else {
        deal.setDealStatus(DealStatus.SCORED);
        dealRepository.save(deal);
      }
    }


  }

  @Override
  @Transactional
  public void cancelDeal(Long productId) {
    Deal currentDeal = dealRepository.findByProductIdAndDealStatusAndDeleteYnFalse(productId, DealStatus.IN_PROGRESS)
            .orElseThrow(() -> new NoSuchElementException("No active deal found for product: " + productId));

    currentDeal.setDealStatus(DealStatus.CANCELED);
    dealRepository.save(currentDeal);

    List<ChatResponseDTO> allChatByProductId = chatRepository.findAllChatByProductId(productId);
    for (ChatResponseDTO chat : allChatByProductId) {
      Chat build = Chat.builder()
              .id(chat.getChatId())
              .message(chat.getMessage())
              .sendDate(new Timestamp(chat.getSendDate().getTime()))
              .deleteYn(true)
              .build();

      chatRepository.save(build);
    }

    // 다음 순번의 거래 찾기
    PageRequest pageRequest = PageRequest.of(0, 1); // 첫 번째 결과만 가져오기
    List<Deal> nextDeals = dealRepository.findNextDealsInQueue(
            productId, DealStatus.BEFORE_DEAL, currentDeal.getWaitingNumber(), pageRequest);


    if (!nextDeals.isEmpty()) {
      Deal dealToActivate = nextDeals.get(0);
      dealToActivate.setDealStatus(DealStatus.IN_PROGRESS);
      dealRepository.save(dealToActivate);

      // 다음 구매자에게 알림
      notifyNextBuyer(dealToActivate);
    }
  }

  private void notifyNextBuyer(Deal dealToActivate) {
    Product product = dealToActivate.getProduct();
    String destination = "/room/" + product.getId();
    ChatMessage cm = new ChatMessage();
    cm.setTitle("your turn");
    cm.setMessage(product.getTitle() + " 상품의 차례가 왔습니다.");
    messagingTemplate.convertAndSend(destination, cm);
  }
}
