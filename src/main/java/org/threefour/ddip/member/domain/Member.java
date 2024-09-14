package org.threefour.ddip.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.threefour.ddip.audit.BaseEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 50)
  private String nickName;

  @Column(nullable = false, length = 50)
  private String school;

  @Column(nullable = false)
  private boolean schoolAuthYn;

  @ColumnDefault("0")
  private boolean deleteYn;
}
