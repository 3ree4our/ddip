package org.threefour.ddip.admin.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.threefour.ddip.audit.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Data
public class Admin extends BaseEntity {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
