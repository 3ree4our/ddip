package org.threefour.ddip.chat.domain.dto;

import lombok.*;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.member.domain.Member;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@NoArgsConstructor
@ToString
public class ChatroomResponseDTO {
  private Long productId;
  private String message;
  private Date sendDate;

  private ChatMemberDTO sender;
  private ChatMemberDTO productOwner;
  private String image;
  private String status;

  public ChatroomResponseDTO(Long productId, String message, Date sendDate, Member sender, Member productOwner) {
    this.productId = productId;
    this.message = message;
    this.sendDate = sendDate;

    this.sender = new ChatMemberDTO(sender.getId(), sender.getNickName(), "SENDER");
    this.productOwner = new ChatMemberDTO(productOwner.getId(), productOwner.getNickName(), "OWNER");
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
