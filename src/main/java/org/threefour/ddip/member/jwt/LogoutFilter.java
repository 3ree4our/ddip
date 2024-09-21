package org.threefour.ddip.member.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;
import org.threefour.ddip.member.repository.RefreshRepository;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    String requestUri = request.getRequestURI();
    if (!requestUri.matches("^\\/logout$")) {
      filterChain.doFilter(request, response);
      return;
    }
    String requestMethod = request.getMethod();
    if (!requestMethod.equals("POST")) {
      filterChain.doFilter(request, response);
      return;
    }

    String refresh = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    if (refresh == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Boolean isExist = refreshRepository.existsByRefreshToken(refresh);
    if (!isExist) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    refreshRepository.deleteByRefreshToken(refresh);

    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");

    response.addCookie(cookie);
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
