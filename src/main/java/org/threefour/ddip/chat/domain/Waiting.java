package org.threefour.ddip.chat.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Waiting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product productId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private Member senderId;

  @ColumnDefault("true")
  private Boolean isPossible;

}
