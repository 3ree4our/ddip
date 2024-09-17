package org.threefour.ddip.product.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.threefour.ddip.audit.BaseGeneralEntity;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.category.domain.ProductCategory;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Product extends BaseGeneralEntity {
    @Column(nullable = false, length = 20)
    private String name;

    @Convert(converter = Price.PriceConverter.class)
    @Column(nullable = false)
    private Price price;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @OneToMany(mappedBy = "product", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true, fetch = LAZY)
    private List<ProductCategory> productCategories;

    @Builder
    private Product(
            Long id, String name, String price, String title, String content, Member seller
    ) {
        this.name = name;
        this.price = Price.of(price);
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

    String getName() {
        return name;
    }

    Price getPrice() {
        return price;
    }

    String getTitle() {
        return title;
    }

    String getContent() {
        return content;
    }

    Member getSeller() {
        return seller;
    }

    List<ProductCategory> getProductCategories() {
        return productCategories;
    }
}
