package org.threefour.ddip.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMemberDTO {
  private Long id;
  private String username;
  private String type;
}
