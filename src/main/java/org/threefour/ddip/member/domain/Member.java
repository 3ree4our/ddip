package org.threefour.ddip.member.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.product.category.domain.ProductCategory;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Data
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

  @OneToMany(mappedBy = "member", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true, fetch = LAZY)
  @ToString.Exclude
  private List<Address> addresses;

  @ManyToMany(fetch = FetchType.EAGER) //다른방법
  @JoinTable(
          name = "member_roles",
          joinColumns = @JoinColumn(name = "member_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles = new ArrayList<>();
}