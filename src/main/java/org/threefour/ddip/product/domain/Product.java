package org.threefour.ddip.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @Builder
    private Product(Long id, String name, int price, String title, String content, Member seller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.title = title;
        this.content = content;
        this.seller = seller;
    }

    public static Product from(RegisterProductRequest registerProductRequest, Member seller) {
        return Product.builder()
                .name(registerProductRequest.getName())
                .price(registerProductRequest.getPrice())
                .title(registerProductRequest.getTitle())
                .content(registerProductRequest.getContent())
                .seller(seller)
                .build();
    }

    public Long getId() {
        return id;
    }
}
