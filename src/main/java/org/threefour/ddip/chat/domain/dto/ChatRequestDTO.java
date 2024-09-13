package org.threefour.ddip.chat.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class ChatRequestDTO {

  private Long receiverId; // 판매자
  private Long senderId; // 구매자
  private Long productId;
  private String message;

}
