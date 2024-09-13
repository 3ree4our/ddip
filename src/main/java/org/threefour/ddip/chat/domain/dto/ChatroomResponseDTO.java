package org.threefour.ddip.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatroomResponseDTO {
  private Long id;
  private String message;
  private Long senderId;
  private Long productId;
  private String productTitle;
}
