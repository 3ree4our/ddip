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
  private Long roomId;
  private String nickname;
  private String message;
  private String title;
  private String type;
}
