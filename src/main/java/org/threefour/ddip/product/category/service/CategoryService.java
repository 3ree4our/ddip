package org.threefour.ddip.product.category.service;

import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.ConnectCategoryRequest;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;
import org.threefour.ddip.product.domain.Product;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

public interface CategoryService {
    void createCategory(RegisterCategoryRequest registerCategoryRequest);

    void createProductCategories(ConnectCategoryRequest connectCategoryRequest, Product product);

    @Transactional(isolation = READ_COMMITTED, timeout = 20)
    void updateProductCategories(ConnectCategoryRequest connectCategoryRequest, Product product);

    List<Category> getCategories(Short parentCategoryId);

    Category getCategory(Short id);

    void deleteCategory(short id);
}
