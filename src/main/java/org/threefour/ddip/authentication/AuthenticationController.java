package org.threefour.ddip.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.util.FormatValidator;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final JWTUtil jwtUtil;

    @PostMapping("/save-member-id")
    public ResponseEntity<Long> saveMemberIdToSession(
            @RequestHeader("Authorization") String authorizationHeader, HttpSession httpSession
    ) {
        String accessToken = authorizationHeader.substring(7).trim();
        if (FormatValidator.hasValue(accessToken) && FormatValidator.containsDot(accessToken)) {
            Long memberId = jwtUtil.getId(accessToken);
            httpSession.setAttribute("memberId", memberId);

            return ResponseEntity.status(CREATED).body(memberId);
        }

        return ResponseEntity.status(CREATED).body(null);
    }
}
