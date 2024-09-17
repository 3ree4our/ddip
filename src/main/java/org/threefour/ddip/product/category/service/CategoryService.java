package org.threefour.ddip.product.category.service;

import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;

public interface CategoryService {
    void createCategory(RegisterCategoryRequest registerCategoryRequest);

    Category getCategory(Short id);
}
