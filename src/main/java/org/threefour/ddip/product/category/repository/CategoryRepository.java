package org.threefour.ddip.product.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.threefour.ddip.product.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Short> {
    @Query("SELECT c FROM Category c WHERE ((:parentCategoryId IS NULL AND c.parentCategory IS NULL) OR c.parentCategory.id = :parentCategoryId) AND c.deleteYn = false")
    List<Category> findByParentCategoryId(@Param("parentCategoryId") Short parentCategoryId);

    Optional<Category> findByIdAndDeleteYnFalse(Short categoryId);
}
