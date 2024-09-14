package org.threefour.ddip.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.threefour.ddip.member.domain.MemberDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
    String username = obtainUsername(request);
    String password = obtainPassword(request);
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
    MemberDetails memberDetails = (MemberDetails) authResult.getPrincipal();
    String username = memberDetails.getUsername();
    Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority authority = iterator.next();

    Long id = memberDetails.getId();

    String token = jwtUtil.createJwt(id, username, /*role, */ 1000 * 60 * 60L);

    //HTTP 인증 방식인 RFC 7235에 맞추어 정의
    //Authorization: Bearer <token>
    //response.addHeader("Authorization", "Bearer " + token);
    Cookie cookie = createCookie(token);
    response.addCookie(cookie);
    response.sendRedirect("/");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private Cookie createCookie(String accessToken) {
    Cookie cookie = new Cookie("access_token", accessToken);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60);
    cookie.setHttpOnly(true);

    return cookie;
  }

}
