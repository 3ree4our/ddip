package org.threefour.ddip.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.threefour.ddip.chat.domain.Waiting;

import java.time.LocalDateTime;

@Getter
@Builder
public class WaitingResponseDTO {
  private Long id;
  private Long productId;
  private Long senderId;
  private String productTitle;
  private String senderNickname;
  private String status;
  private LocalDateTime createdAt;

  public static WaitingResponseDTO from(Waiting waiting) {
    return WaitingResponseDTO.builder()
            .id(waiting.getId())
            .productId(waiting.getProduct().getId())
            .senderId(waiting.getSender().getId())
            .productTitle(waiting.getProduct().getTitle())
            .senderNickname(waiting.getSender().getNickName())
            .status(waiting.getStatus().name())
            .createdAt(waiting.getCreatedAt())
            .build();
  }

}
