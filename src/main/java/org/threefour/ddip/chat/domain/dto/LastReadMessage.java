package org.threefour.ddip.chat.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LastReadMessage {
  @EmbeddedId
  private LastReadMessageId id;

  @Column(name = "last_read_id")
  private Long lastReadId;
}