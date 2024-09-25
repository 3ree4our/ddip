package org.threefour.ddip.address.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Data
public class Address extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, length = 5)
  private String zipcode;

  @Column(nullable = false, length = 50)
  private String roadAddress;

  @Column(nullable = false)
  private String detailedAddress;

  @Column(nullable = false)
  private Double lat;

  @Column(nullable = false)
  private Double lnt;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  @JsonBackReference
  private Member member;
}
