package org.threefour.ddip.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.threefour.ddip.chat.domain.Chat;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatResponseDTO {

  private Long productId;
  private String message;
  private String productName;
  private String senderNickname;
  private Date sendDate;
}