package org.threefour.ddip.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.threefour.ddip.chat.domain.dto.WaitingResponseDTO;
import org.threefour.ddip.chat.service.ChatService;
import org.threefour.ddip.chat.service.WaitingService;
import org.threefour.ddip.member.domain.MemberDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class WaitingAPIController {

  private final WaitingService waitingService;
  private final ChatService chatService;

  @PostMapping("/waitings")
  public ResponseEntity<WaitingResponseDTO> creatWaiting(@RequestParam Long productId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {

    Long senderId = ((MemberDetails) userDetails).getId();

    if (!waitingService.isProductAvailableForChat(productId)) {
      return ResponseEntity.badRequest().body(null);
    }

    WaitingResponseDTO watiting = waitingService.createWatiting(productId, senderId);
    return ResponseEntity.ok(watiting);
  }

  @GetMapping("/waitings")
  public ResponseEntity<List<WaitingResponseDTO>> getWaitingList(@RequestParam Long productId) {
    List<WaitingResponseDTO> waitingListForProduct = waitingService.getWaitingListForProduct(productId);
    return ResponseEntity.ok(waitingListForProduct);
  }

  @PostMapping("/waitings/{waitingId}")
  public ResponseEntity<Void> updateWaitingStatus(@PathVariable Long waitingId,
                                                  @RequestParam String status) {
    if ("accepted".equalsIgnoreCase(status)) {
      waitingService.acceptWaiting(waitingId);
    } else if ("rejected".equalsIgnoreCase(status)) {
      waitingService.rejectWaiting(waitingId);
    } else {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok().build();
  }

  @GetMapping("/products/{productId}/status")
  public ResponseEntity<Boolean> getChatAvailability(@PathVariable Long productId) {
    boolean isAvailable = waitingService.isProductAvailableForChat(productId);
    return ResponseEntity.ok(isAvailable);
  }

  @GetMapping("/waitings/me")
  public ResponseEntity<List<WaitingResponseDTO>> getUserWaitings(@AuthenticationPrincipal UserDetails userDetails) {
    Long senderId = ((MemberDetails) userDetails).getId();
    List<WaitingResponseDTO> waitingList = waitingService.getWaitingListForSender(senderId);
    return ResponseEntity.ok(waitingList);
  }

  @PostMapping("/sessions")
  public ResponseEntity<Void> createChatSession(@RequestParam Long waitingId) {
    waitingService.startChat(waitingId);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/waitings/count")
  public ResponseEntity<Long> getWaitingCount(@RequestParam Long productId) {
    Long count = waitingService.getWaitingCountForProduct(productId);
    return ResponseEntity.ok(count);
  }

}
