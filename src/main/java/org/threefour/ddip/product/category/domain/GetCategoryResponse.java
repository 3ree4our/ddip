package org.threefour.ddip.product.category.domain;

import lombok.Getter;

@Getter
public class GetCategoryResponse {
    private Short id;
    private CategoryName name;

    private GetCategoryResponse(Short id, CategoryName name) {
        this.id = id;
        this.name = name;
    }

    public static GetCategoryResponse from(Category category) {
        return new GetCategoryResponse(category.getId(), category.getName());
    }

    public String getDescription() {
        return name.getDescription();
    }
}
