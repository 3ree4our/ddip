package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;
import org.threefour.ddip.chat.domain.dto.ProductResponseDTO;
import org.threefour.ddip.chat.service.ChatService;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.deal.domain.DealStatus;
import org.threefour.ddip.deal.service.DealService;
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
  private final DealService dealService;
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
    Long userId = jwtUtil.getId(accessToken);

    List<ChatroomResponseDTO> activeChats = chatService.findAllActiveChatsForUser(userId);

    return new ResponseEntity<>(activeChats, HttpStatus.OK);
  }

  @GetMapping("/user/chatrooms")
  public ResponseEntity<List<Long>> getUserChatrooms(@RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<Long> productIds = dealService.getProductIdsForUserChats(id);
    return ResponseEntity.ok(productIds);
  }

  @GetMapping("/chatrooms/{productId}/s3")
  public ResponseEntity<List<ChatResponseDTO>> getChatroomByProductIdWithS3(
          @PathVariable("productId") Long productId,
          @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<ChatResponseDTO> allChatByProductId = chatService.findAllChatByProductId(productId);

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

  @GetMapping("/chatrooms/{productId}")
  public ResponseEntity<List<ChatResponseDTO>> getChatroomByProductIdWithLocal(
          @PathVariable("productId") Long productId,
          @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    List<ChatResponseDTO> allChatByProductId = chatService.findAllChatByProductId(productId);

    List<ChatResponseDTO> list = new ArrayList<>();
    for (ChatResponseDTO chat : allChatByProductId) {
      DealStatus dealStatus = dealService.getProductIdAndDealStatusAndDeleteYnFalse(chat.getProductId(), DealStatus.PAID);
      chat.setStatus(dealStatus.name());

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

  @GetMapping("/chatrooms/total-unread-count")
  public ResponseEntity<Integer> getTotalUnreadCount(@RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);

    int totalUnreadCount = chatService.getTotalUnreadMessageCount(id);
    return ResponseEntity.ok(totalUnreadCount);
  }

  @PostMapping("/{productId}/mark-read")
  public ResponseEntity<Void> markAsRead(@PathVariable Long productId, @RequestHeader("Authorization") String token) {
    String accessToken = token.substring(7);
    Long id = jwtUtil.getId(accessToken);
    chatService.markAsRead(productId, id);
    return ResponseEntity.ok().build();
  }


}
