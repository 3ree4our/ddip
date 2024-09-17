package org.threefour.ddip.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.product.domain.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeleteYnFalse(Long productId);
}
