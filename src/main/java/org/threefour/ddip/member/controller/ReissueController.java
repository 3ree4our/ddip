package org.threefour.ddip.member.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threefour.ddip.member.domain.Refresh;
import org.threefour.ddip.member.jwt.JWTUtil;
import org.threefour.ddip.member.repository.RefreshRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  /*refresh로 access 발급*/
  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    String refresh = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    if (refresh == null) {
      return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
    }

    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
    }

    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
      return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    }

    Boolean isExist = refreshRepository.existsByRefreshToken(refresh);
    if (!isExist) {
      return new ResponseEntity<>("invalid refresh tokne", HttpStatus.BAD_REQUEST);
    }

    Long id = jwtUtil.getId(refresh);
    String username = jwtUtil.getUsername(refresh);
    String nickname = jwtUtil.getNickname(refresh);
    //String role = jwtUtil.getRole(refresh);

    String newAccess = jwtUtil.createJwt(id, "access", username, nickname, /*role,*/ 600000L);
    String newRefresh = jwtUtil.createJwt(id, "refresh", username, nickname, 43200000L);

    refreshRepository.deleteByRefreshToken(refresh);
    addRefreshEntity(username, newRefresh, 8640000L);

    response.addHeader("access", newAccess);
    response.addCookie(createCookie("refresh", newRefresh));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Cookie createCookie(String name, String value) {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(24 * 60 * 60);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    return cookie;
  }

  private void addRefreshEntity(String username, String refresh, Long expiredMs) {
    Date date = new Date(System.currentTimeMillis() + expiredMs);

    Refresh refreshEntity = new Refresh();
    refreshEntity.setUsername(username);
    refreshEntity.setRefreshToken(refresh);
    refreshEntity.setExpiration(date.toString());

    refreshRepository.save(refreshEntity);
  }
}