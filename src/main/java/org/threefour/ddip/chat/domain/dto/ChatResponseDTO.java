package org.threefour.ddip.chat.domain.dto;

import lombok.*;
import org.threefour.ddip.chat.domain.Chat;
import org.threefour.ddip.member.domain.Member;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatResponseDTO {
  private Long chatId;
  private Long productId;
  private String message;
  private String productName;
  private String type;
  private ChatMemberDTO sender;
  private ChatMemberDTO productOwner;
  private Date sendDate;

  private List<Long> chatImageIds = new ArrayList<>();

  public ChatResponseDTO(Long chatId, Long productId, String message, String productName, Member productOwner, Member sender, Date sendDate) {
    this.chatId = chatId;
    this.productId = productId;
    this.message = message;
    this.productName = productName;
    this.sender = new ChatMemberDTO(sender.getId(), sender.getNickName(), "SENDER");
    this.productOwner = new ChatMemberDTO(productOwner.getId(), productOwner.getNickName(), "OWNER");
    this.sendDate = sendDate;
  }
}