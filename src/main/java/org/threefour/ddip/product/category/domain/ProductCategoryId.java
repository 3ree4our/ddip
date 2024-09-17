package org.threefour.ddip.product.category.domain;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public class ProductCategoryId implements Serializable {
    private Short category;
    private Long product;

    public ProductCategoryId(Short category, Long product) {
        this.category = category;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryId that = (ProductCategoryId) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, product);
    }
}
