package org.threefour.ddip.product.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.threefour.ddip.product.category.domain.ProductCategory;
import org.threefour.ddip.product.category.domain.ProductCategoryId;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    void deleteAllByProductId(@Param("productId") Long productId);
}
