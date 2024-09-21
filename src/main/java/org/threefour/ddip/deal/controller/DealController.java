package org.threefour.ddip.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.DealStatus;
import org.threefour.ddip.deal.domain.InitializeDealRequest;
import org.threefour.ddip.deal.service.DealService;
import org.threefour.ddip.exception.TokenNoValueException;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.FormatValidator;
import org.threefour.ddip.util.PageableGenerator;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.threefour.ddip.exception.ExceptionMessage.TOKEN_NO_VALUE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.util.PaginationConstant.*;

@Controller
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
  private final JWTUtil jwtUtil;
  private final DealService dealService;
  private final ProductService productService;

  @PostMapping("/initialize")
  public ResponseEntity<Integer> initializeDeal(
          @RequestBody InitializeDealRequest initializeDealRequest,
          @RequestHeader("Authorization") String authorizationHeader, HttpSession httpSession
  ) {
    String accessToken = authorizationHeader.substring(7).trim();
    if (!FormatValidator.hasValue(accessToken)) {
      throw new TokenNoValueException(TOKEN_NO_VALUE_EXCEPTION_MESSAGE);
    }

    int waitingNumber = dealService.createDeal(jwtUtil.getId(accessToken), initializeDealRequest);
    httpSession.setAttribute("waitingNumber", waitingNumber);

    return ResponseEntity.status(CREATED).body(waitingNumber);
  }

  @GetMapping("/products/{productId}")
  public ResponseEntity<Integer> checkActiveDeal(@PathVariable Long productId,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
    String accessToken = authorizationHeader.substring(7).trim();
    if (!FormatValidator.hasValue(accessToken)) {
      throw new TokenNoValueException(TOKEN_NO_VALUE_EXCEPTION_MESSAGE);
    }

    int waitingCount = dealService.getWaitingNumberCount(productId);
    return ResponseEntity.ok(waitingCount);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<?> checkTarget(@PathVariable Long productId,
                                       @RequestHeader("Authorization") String authorizationHeader) {
    String accessToken = authorizationHeader.substring(7).trim();
    if (!FormatValidator.hasValue(accessToken)) {
      throw new TokenNoValueException(TOKEN_NO_VALUE_EXCEPTION_MESSAGE);
    }
    Long memberId = jwtUtil.getId(accessToken);
    Product product = productService.getProduct(productId, false);
    Map<String, Object> response = new HashMap<>();

    if (product.getId() == memberId) {
      response.put("dealStatus", "NO");
      response.put("message", "자신의 상품은 구매가 불가능 합니다.");
      return ResponseEntity.badRequest().body("자신의 상품은 구매가 불가능 합니다.");
    }

    Deal deal = dealService.checkWaitingStatus(productId, memberId);

    if (deal != null) {
      DealStatus dealStatus = deal.getDealStatus();
      response.put("dealStatus", dealStatus);
      response.put("message", getMessageForStatus(dealStatus));
      response.put("waitingNum", deal.getWaitingNumber());
    } else {
      response.put("dealStatus", "GO");
      response.put("message", "구매 가능한 상태입니다.");
    }

    return ResponseEntity.ok(response);
  }

  @PostMapping("/{productId}/complete")
  public ResponseEntity<Void> completeDeal(@PathVariable Long productId) {
    dealService.completeDeal(productId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{productId}/cancel")
  public ResponseEntity<Void> cancelDeal(@PathVariable Long productId) {
    dealService.cancelDeal(productId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/purchases")
  public ModelAndView getBoughtDeals(
          @RequestParam("buyerId") String buyerId,
          @RequestParam(defaultValue = TRUE) String paged,
          @RequestParam(defaultValue = ZERO) String pageNumber,
          @RequestParam(defaultValue = NINE) String size,
          @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort
  ) {
    Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);

    return new ModelAndView("deal/purchases");
  }

  private String getMessageForStatus(DealStatus status) {
    switch (status) {
      case IN_PROGRESS:
        return "현재 거래가 진행 중입니다. 채팅방으로 이동할 수 있습니다.";
      case BEFORE_DEAL:
        return "구매 신청이 완료되었습니다. 대기 중입니다.";
      case CANCELED:
        return "거래가 취소되었습니다. 새로운 구매 신청이 가능합니다.";
      case BEFORE_PAYMENT:
        return "결제 대기 중입니다.";
      case PAID:
        return "결제가 완료되었습니다.";
      case SCORED:
        return "거래가 완료되었습니다.";
      case REFUNDED:
        return "환불이 완료되었습니다.";
      default:
        return "알 수 없는 상태입니다.";
    }
  }
}
