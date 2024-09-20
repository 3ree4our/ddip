package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;
import org.threefour.ddip.chat.domain.dto.ProductResponseDTO;
import org.threefour.ddip.chat.service.ChatService;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;
import org.threefour.ddip.image.service.ImageLocalServiceImpl;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.member.jwt.JWTUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatAPIController {

  private final ChatService chatService;
  private final ImageLocalServiceImpl imageLocalService;
  private final ImageService imageService;
  private final JWTUtil jwtUtil;

  @GetMapping("/products")
  public ResponseEntity<List<ProductResponseDTO>> getAllProductByMemberId(@RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);
    List<ProductResponseDTO> list = chatService.getAllProductBySellerId(id);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/chatrooms")
  public ResponseEntity<List<ChatroomResponseDTO>> getAllChatroom(@RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);
    Map<Long, ChatroomResponseDTO> chatMap = new HashMap<>();

    List<ProductResponseDTO> allProductBySellerId = chatService.getAllProductBySellerId(id);
    for (ProductResponseDTO product : allProductBySellerId) {
      ChatroomResponseDTO chatByProductId = chatService.findChatByProductId(product.getProductId());
      if (chatByProductId != null) {
        updateChatMap(chatMap, chatByProductId);
      }
    }

    List<ChatroomResponseDTO> chatByOwnerId = chatService.findAllChatByOwnerId(id);
    if (chatByOwnerId != null) {
      for (ChatroomResponseDTO chat : chatByOwnerId) {
        updateChatMap(chatMap, chat);
      }
    }

    List<ChatroomResponseDTO> result = new ArrayList<>(chatMap.values());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/chatrooms/{chatroomId}/s3")
  public ResponseEntity<List<ChatResponseDTO>> getChatroomByProductIdWithS3(
          @PathVariable("chatroomId") Long chatroomId,
          @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<ChatResponseDTO> allChatByProductId = chatService.findAllChatByProductId(chatroomId);

    List<ChatResponseDTO> list = new ArrayList<>();
    for (ChatResponseDTO chat : allChatByProductId) {

      if (chat.getSender().getId() != id) chat.setType("left");
      else chat.setType("right");

      List<Image> images = imageService.getImages(TargetType.CHATTING, chat.getChatId());

      for (Image image : images) {
        //chat.getChatImageIds().add(image.getS3Url());
      }

      list.add(chat);
    }

    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/chatrooms/{chatroomId}")
  public ResponseEntity<List<ChatResponseDTO>> getChatroomByProductIdWithLocal(
          @PathVariable("chatroomId") Long chatroomId,
          @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<ChatResponseDTO> allChatByProductId = chatService.findAllChatByProductId(chatroomId);

    List<ChatResponseDTO> list = new ArrayList<>();
    for (ChatResponseDTO chat : allChatByProductId) {

      if (chat.getSender().getId() != id) chat.setType("left");
      else chat.setType("right");

      List<Long> imageByChatId = imageLocalService.getImageByChatId(chat.getChatId());

      for (Long imageId : imageByChatId) {
        chat.getChatImageIds().add(imageId);
      }

      list.add(chat);
    }

    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{productId}/unread-count")
  public ResponseEntity<Integer> getUnreadCount(@PathVariable Long productId, @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    int count = chatService.getUnreadMessageCount(productId, id);
    return ResponseEntity.ok(count);
  }

  @PostMapping("/{productId}/mark-read")
  public ResponseEntity<Void> markAsRead(@PathVariable Long productId, @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);
    chatService.markAsRead(productId, id);
    return ResponseEntity.ok().build();
  }

  private void updateChatMap(Map<Long, ChatroomResponseDTO> chatMap, ChatroomResponseDTO newChat) {
    Long productId = newChat.getProductId();
    if (!chatMap.containsKey(productId) || newChat.getSendDate().after(chatMap.get(productId).getSendDate())) {
      chatMap.put(productId, newChat);
    }
  }


}
