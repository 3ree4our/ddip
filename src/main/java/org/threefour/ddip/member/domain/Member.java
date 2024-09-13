package org.threefour.ddip.member.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.threefour.ddip.audit.BaseEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickName;

  @Column(nullable = false)
  private String school;

  private int schoolAuthYn;

  @ColumnDefault("0")
  private int deleteYn;
}