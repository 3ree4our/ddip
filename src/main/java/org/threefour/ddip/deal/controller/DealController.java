package org.threefour.ddip.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.threefour.ddip.deal.domain.InitializeDealRequest;
import org.threefour.ddip.deal.service.DealService;
import org.threefour.ddip.exception.TokenNoValueException;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.util.FormatValidator;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;
import static org.threefour.ddip.exception.ExceptionMessage.TOKEN_NO_VALUE_EXCEPTION_MESSAGE;

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

        int waitingNumber = dealService.createDeal(jwtUtil.getId(accessToken), initializeDealRequest);
        httpSession.setAttribute("waitingNumber", waitingNumber);

        return ResponseEntity.status(CREATED).body(waitingNumber);
    }
}
