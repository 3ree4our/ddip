package org.threefour.ddip.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.threefour.ddip.chat.domain.Chat;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatResponseDTO {

  private Long productId;
  private String message;
  private String productName;
  private String senderNickname;

  private Timestamp sendDate;

  // 채팅이니 상품명, 상대방이름 추가하기
  // 이름은 product로 하나 찾고, chat owner로 .. 하나씩 넣어주면 되나?
  public ChatResponseDTO(Chat chat) {
    this.productId = chat.getProductId().getId();
    this.message = chat.getMessage();
    this.sendDate = chat.getSendDate();
  }

}