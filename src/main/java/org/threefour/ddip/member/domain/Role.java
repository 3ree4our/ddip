package org.threefour.ddip.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.threefour.ddip.audit.BaseEntity;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoleType role;

  public enum RoleType {
    ROLE_USER, ROLE_TEACHER, ROLE_ADMIN
  }
}