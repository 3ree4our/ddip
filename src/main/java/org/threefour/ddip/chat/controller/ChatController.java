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

  private final JWTUtil jwtUtil;
  private final ChatService chatService;
  private ConcurrentHashMap<Long, Chat> chatMap = new ConcurrentHashMap<>(); // 나중에 ..

  @GetMapping("/chatrooms")
  public String moveTochatList(String u) {
    // 채팅 리스트만 가져오기
    return "chat/chat-list";
  }

  @GetMapping("/chatrooms/{productId}")
  public String moveTochatListWithProductId(@PathVariable Long productId) {
    // 채팅만 가져오기
    return "chat/chat-list";
  }

}
