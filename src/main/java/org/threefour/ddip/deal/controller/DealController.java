package org.threefour.ddip.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.deal.domain.InitializeDealRequest;
import org.threefour.ddip.deal.exception.IdenticalSellerException;
import org.threefour.ddip.deal.service.DealService;
import org.threefour.ddip.exception.TokenNoValueException;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.util.FormatValidator;
import org.threefour.ddip.util.PageableGenerator;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;
import static org.threefour.ddip.deal.exception.ExceptionMessage.IDENTICAL_SELLER_EXCEPTION_MESSAGE;
import static org.threefour.ddip.exception.ExceptionMessage.TOKEN_NO_VALUE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.util.PaginationConstant.*;

@Controller
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final JWTUtil jwtUtil;
    private final DealService dealService;

    @PostMapping("/initialize")
    public ResponseEntity<Integer> initializeDeal(
            @RequestBody InitializeDealRequest initializeDealRequest,
            @RequestHeader("Authorization") String authorizationHeader, HttpSession httpSession
    ) {
        String accessToken = authorizationHeader.substring(7).trim();
        if (!FormatValidator.hasValue(accessToken)) {
            throw new TokenNoValueException(TOKEN_NO_VALUE_EXCEPTION_MESSAGE);
        }

        Long buyerId = jwtUtil.getId(accessToken);
        if (buyerId.toString().equals(initializeDealRequest.getSellerId())) {
            throw new IdenticalSellerException(IDENTICAL_SELLER_EXCEPTION_MESSAGE);
        }
        int waitingNumber = dealService.createDeal(jwtUtil.getId(accessToken), initializeDealRequest);
        httpSession.setAttribute("waitingNumber", waitingNumber);

        return ResponseEntity.status(CREATED).body(waitingNumber);
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<Void> checkTarget(@PathVariable Long sellerId, @RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.substring(7).trim();
        if (!FormatValidator.hasValue(accessToken)) {
            throw new TokenNoValueException(TOKEN_NO_VALUE_EXCEPTION_MESSAGE);
        }

        Long buyerId = jwtUtil.getId(accessToken);
        if (sellerId != buyerId) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
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
}
