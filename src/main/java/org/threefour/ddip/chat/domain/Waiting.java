package org.threefour.ddip.chat.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Waiting extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private Member sender;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WaitingStatus status;

  public void updateStatus(WaitingStatus status) {
    this.status = status;
  }
}
