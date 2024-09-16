package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.threefour.ddip.chat.domain.ChatMessage;
import org.threefour.ddip.chat.domain.Waiting;
import org.threefour.ddip.chat.domain.dto.ChatRequestDTO;
import org.threefour.ddip.chat.domain.dto.ProductResponseDTO;
import org.threefour.ddip.chat.domain.dto.WaitingRequestDTO;
import org.threefour.ddip.chat.service.ChatService;
import org.threefour.ddip.chat.service.WaitingService;
import org.threefour.ddip.member.domain.MemberDetails;
import org.threefour.ddip.member.service.MemberDetailsService;
import org.threefour.ddip.product.service.ProductServiceImpl;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

  private final MemberDetailsService memberDetailsService;
  private final ProductServiceImpl productService;
  private final ChatService chatService;
  private final WaitingService waitingService;

  @MessageMapping("/{productId}")
  @SendTo("/room/{productId}")
  public ChatMessage sendMessage(
          @DestinationVariable("productId") String productId,
          ChatMessage message,
          Principal principal) {

    String username = principal.getName();

    long pi = Long.parseLong(productId);
    ProductResponseDTO productByProductId = productService.getProductByProductId(pi);

    MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(username);
    String nickName = memberDetails.getNickName();

    String type = "";
    if (username != null) type = "right";
    else type = "left";

    ChatMessage mg = ChatMessage.builder()
            .roomId(productByProductId.getProductId())
            .nickname(nickName)
            .type(type)
            .message(message.getMessage())
            .title(productByProductId.getTitle())
            .build();


    ChatRequestDTO dto = ChatRequestDTO.builder()
            .owner(memberDetails.getId())
            .productId(pi)
            .message(message.getMessage())
            .build();

    // 채팅 저장
    //Waiting chatByProductId = waitingService.getChatByProductId(productId);
    //WaitingRequestDTO waitingRequestDTO = new WaitingRequestDTO();
    //if (chatByProductId != null) {
    chatService.createChat(dto);
    //  waitingRequestDTO.setProductId(productId);
    //  waitingRequestDTO.setSenderId(buyerId);
    //
    //  waitingService.addWaiting(waitingRequestDTO);
    //}else{
    //
    //}

    return mg;
  }

}
