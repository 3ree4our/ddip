package org.threefour.ddip.chat.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Chat extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /*@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false, updatable = false)
  private Member senderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false, updatable = false)
  private Member receiverId;*/

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false, updatable = false)
  private Member owner;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false, updatable = false)
  private Product productId;

  @Column(length = 1000)
  private String message;

  @CreationTimestamp
  private Timestamp sendDate;

  @ColumnDefault("0")
  @Column(name = "delete_yn")
  private boolean deleteYn;

}
