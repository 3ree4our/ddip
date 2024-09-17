package org.threefour.ddip.product.category.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCategoryRequest {
    private String parentCategoryId;
    private CategoryName categoryName;
}
