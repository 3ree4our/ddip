package org.threefour.ddip.chat.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.threefour.ddip.member.domain.Member;

@Getter
@Setter
public class WaitingRequestDTO {
  private Long productId;
  private Member senderId;
  private Boolean isPossible;
}
