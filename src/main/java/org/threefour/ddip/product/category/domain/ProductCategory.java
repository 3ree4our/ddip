package org.threefour.ddip.product.category.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@IdClass(ProductCategoryId.class)
@NoArgsConstructor(access = PROTECTED)
public class ProductCategory {
    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_category_id"))
    private Category category;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_product_id"))
    private Product product;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private Timestamp createdAt;

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
