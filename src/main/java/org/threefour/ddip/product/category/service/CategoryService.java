package org.threefour.ddip.product.category.service;

import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.ConnectCategoryRequest;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;
import org.threefour.ddip.product.domain.Product;

import java.util.List;

public interface CategoryService {
    void createCategory(RegisterCategoryRequest registerCategoryRequest);

    void createProductCategories(ConnectCategoryRequest connectCategoryRequest, Product product);

    void updateProductCategories(ConnectCategoryRequest connectCategoryRequest, Product product);

    List<Category> getCategories(Short parentCategoryId);

    Category getCategory(Short id);

    void deleteCategory(short id);
}
