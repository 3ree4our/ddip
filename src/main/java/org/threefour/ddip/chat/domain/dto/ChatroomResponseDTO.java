package org.threefour.ddip.chat.domain.dto;

import lombok.*;
import org.threefour.ddip.chat.domain.Chat;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@ToString
public class ChatroomResponseDTO {
  private Long productId;
  private String message;
  private Timestamp sendDate;

  // 채팅룸이니 상품명 추가하기
  public ChatroomResponseDTO(Chat chat) {
    this.productId = chat.getProductId().getId();
    this.message = chat.getMessage();
    this.sendDate = chat.getSendDate();
  }

}
