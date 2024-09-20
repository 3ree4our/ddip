package org.threefour.ddip.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.domain.MemberDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
    //String role = jwtUtil.getRole(accessToken);

    Member member = new Member();
    member.setEmail(username);
    //member.setRole(role);

    MemberDetails memberDetails = new MemberDetails(member);

    Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}