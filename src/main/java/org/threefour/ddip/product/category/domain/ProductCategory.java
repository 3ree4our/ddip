package org.threefour.ddip.product.category.domain;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.threefour.ddip.audit.BaseEntity;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@IdClass(ProductCategoryId.class)
@NoArgsConstructor(access = PROTECTED)
public class ProductCategory extends BaseEntity {
    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_category_id"))
    private Category category;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_product_id"))
    private Product product;

    private ProductCategory(Category category, Product product) {
        this.category = category;
        this.product = product;
    }

    public static ProductCategory of(Category category, Product product) {
        return new ProductCategory(category, product);
    }

    Category getCategory() {
        return category;
    }
}
