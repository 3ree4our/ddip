package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.threefour.ddip.chat.domain.ChatMessage;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;
import org.threefour.ddip.chat.service.ChatService;
import org.threefour.ddip.chat.service.WaitingService;
import org.threefour.ddip.image.service.ImageLocalServiceImpl;
import org.threefour.ddip.member.domain.MemberDetails;
import org.threefour.ddip.member.service.MemberDetailsService;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.service.ProductServiceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

  private final MemberDetailsService memberDetailsService;
  private final ProductServiceImpl productService;
  private final ChatService chatService;
  private final ImageLocalServiceImpl imageLocalService;
  private final WaitingService waitingService;

  @MessageMapping("/{productId}")
  @SendTo("/room/{productId}")
  public ChatMessage sendMessage(
          @DestinationVariable("productId") String productId,
          ChatMessage message,
          Principal principal) {

    String username = principal.getName();

    long pi = Long.parseLong(productId);
    Product productByProductId = productService.getProduct(pi);

    MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(username);
    String nickName = memberDetails.getNickName();

    ChatRequestDTO dto = ChatRequestDTO.builder()
            .owner(memberDetails.getId())
            .productId(pi)
            .message(message.getMessage())
            .build();

    Long saveId = chatService.createChat(dto);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String format = formatter.format(LocalDateTime.now());

    // 이미지 처리
    List<Long> imageIds = imageLocalService.getImageByChatId(saveId);

    ChatMessage mg = ChatMessage.builder()
            .roomId(productByProductId.getId())
            .messageId(saveId)
            .nickname(nickName)
            .title(productByProductId.getTitle())
            .message(message.getMessage())
            .sendDate(format)
            .build();


    return mg;
  }

}
