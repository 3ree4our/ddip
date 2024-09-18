package org.threefour.ddip.product.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.product.category.domain.ProductCategory;
import org.threefour.ddip.product.category.domain.ProductCategoryId;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    List<ProductCategory> findByProductId(Long productId);

    void deleteAllByProductId(Long id);
}
