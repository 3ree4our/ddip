package org.threefour.ddip.chat.domain.dto;

import lombok.*;
import org.threefour.ddip.chat.domain.Chat;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatroomResponseDTO {
  private Long productId;
  private String message;
  private Date sendDate;

}
