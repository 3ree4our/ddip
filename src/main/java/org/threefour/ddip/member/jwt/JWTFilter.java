package org.threefour.ddip.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String accessToken = request.getHeader("access");

    if(accessToken == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      jwtUtil.isExpired(accessToken);
    }catch (Exception e) {
      PrintWriter writer = response.getWriter();
      writer.println(e.getMessage());

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String catecory = jwtUtil.getCategory(accessToken);

    if(!catecory.equals("access")){
      PrintWriter writer = response.getWriter();
      writer.println("invalid access token");

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String username = jwtUtil.getUsername(accessToken);
    List<String> role = jwtUtil.getRole(accessToken);

    List<SimpleGrantedAuthority> authorities = role.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    Authentication authToken = new UsernamePasswordAuthenticationToken(username/*memberDetails*/, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}