package org.threefour.ddip.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MemberApiController {

  @GetMapping("/access")
  public void access(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().startsWith("access")) {
        String value = cookie.getValue();
        Cookie removeAccessCookie = new Cookie("access_token", "");
        removeAccessCookie.setPath("/");
        removeAccessCookie.setMaxAge(0);

        response.addCookie(removeAccessCookie);
        response.setHeader("accessToken", value);
      }
    }
  }

}
