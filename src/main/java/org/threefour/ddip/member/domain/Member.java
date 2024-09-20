package org.threefour.ddip.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Data
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
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