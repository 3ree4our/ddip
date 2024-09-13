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

  private Long id;
  private String message;
  private Timestamp sendDate;

  public ChatResponseDTO(Chat chat) {
    this.id = chat.getId();
    this.message = chat.getMessage();
    this.sendDate = chat.getSendDate();
  }

}