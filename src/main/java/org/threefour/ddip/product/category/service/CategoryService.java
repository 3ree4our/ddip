package org.threefour.ddip.product.category.service;

import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;

import java.util.List;

public interface CategoryService {
    void createCategory(RegisterCategoryRequest registerCategoryRequest);

    List<Category> getCategories(Short parentCategoryId);

    Category getCategory(Short id);
}
