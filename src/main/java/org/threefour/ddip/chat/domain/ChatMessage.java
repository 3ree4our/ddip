package org.threefour.ddip.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
  private Long roomId;
  private Long messageId;
  private String nickname;
  private String message;
  private String title;
  private String sendDate;
}
