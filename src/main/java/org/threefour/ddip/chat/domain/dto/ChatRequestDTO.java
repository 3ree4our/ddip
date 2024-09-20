package org.threefour.ddip.chat.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class ChatRequestDTO {

  private Long owner; // 채팅방주인
  private Long productId;
  private String message;

}
