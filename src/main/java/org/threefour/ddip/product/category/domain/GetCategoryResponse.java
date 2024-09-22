package org.threefour.ddip.product.category.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class GetCategoryResponse implements Serializable {
    private static final long serialVersionUID = 4L;

    private Short id;
    private CategoryName name;

    private GetCategoryResponse(CategoryName name) {
        this.name = name;
    }

    private GetCategoryResponse(Short id, CategoryName name) {
        this.id = id;
        this.name = name;
    }

    public static GetCategoryResponse from(String description) {
        return new GetCategoryResponse(CategoryName.from(description));
    }

    public static GetCategoryResponse from(Category category) {
        return new GetCategoryResponse(category.getId(), category.getName());
    }

    public String getDescription() {
        return name.getDescription();
    }
}
