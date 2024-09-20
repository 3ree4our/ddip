package org.threefour.ddip.chat.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatMessage {
  private Long roomId;
  private Long messageId;
  private String nickname;
  private String message;
  private String title;
  private boolean isImages;
  private String sendDate;
}
