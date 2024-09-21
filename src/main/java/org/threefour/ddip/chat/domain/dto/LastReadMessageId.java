package org.threefour.ddip.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastReadMessageId implements Serializable {

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "owner_id")
  private Long ownerId;
}
