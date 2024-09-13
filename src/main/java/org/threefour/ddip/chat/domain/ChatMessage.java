package org.threefour.ddip.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
  private int roomId;
  private Long senderId;
  private String message;
  private String title;
  private boolean isMine = false;

  public void setMine(boolean isMine) {
    this.isMine = isMine;
  }
}
