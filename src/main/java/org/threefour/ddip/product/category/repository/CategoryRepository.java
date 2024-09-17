package org.threefour.ddip.product.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.product.category.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Short> {
    Optional<Category> findByIdAndDeleteYnFalse(Short categoryId);
}
