package org.threefour.ddip.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.threefour.ddip.member.domain.MemberDetails;
import org.threefour.ddip.member.domain.Refresh;
import org.threefour.ddip.member.repository.RefreshRepository;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
    String username = obtainUsername(request);
    String password = obtainPassword(request);
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException{
    String username = authResult.getName();
    MemberDetails memberDetails = (MemberDetails) authResult.getPrincipal();
    String nickname = memberDetails.getNickname();

    String access = jwtUtil.createJwt(memberDetails.getId(),"access", username, nickname, 600000L);
    String refresh = jwtUtil.createJwt(memberDetails.getId(),"refresh", username, nickname, 43200000L);
    addRefreshEntity(username, refresh, 86400000L);

    response.addHeader("access", access);
    response.addCookie(createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());

    response.setContentType("application/json; charset=utf-8");
    response.getWriter().write("{\"nickname\":\"" + nickname +  "\", \"success\": true, \"accessToken\": \"" + access + "\"}");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
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