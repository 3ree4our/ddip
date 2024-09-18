package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.chat.service.ChatService;
import org.threefour.ddip.member.jwt.JWTUtil;

import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  @GetMapping("/chat-list")
  public String moveTochatList() {
    return "chat/chat-list";
  }

  @GetMapping("/chat/{productId}")
  public String moveTochatListWithProductId(@PathVariable Long productId) {
    return "chat/chat-list";
  }
}
