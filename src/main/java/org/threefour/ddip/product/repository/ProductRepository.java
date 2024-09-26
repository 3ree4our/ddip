package org.threefour.ddip.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.threefour.ddip.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeleteYnFalse(Long productId);

    Page<Product> findByDeleteYnFalse(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productCategories pc WHERE pc.category.id = :categoryId AND p.deleteYn = false")
    Page<Product> findByCategoryIdAndDeleteYnFalse(@Param("categoryId") Short categoryId, Pageable pageable);

    // 나중에 말하기
    List<Product> findBySellerId(Long sellerId);
}
