package org.threefour.ddip.product.category.domain;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetCategoriesResponse {
    private List<GetCategoryResponse> getCategoryResponses;

    private GetCategoriesResponse(List<GetCategoryResponse> getCategoryResponses) {
        this.getCategoryResponses = getCategoryResponses;
    }

    public static GetCategoriesResponse from(List<Category> categories) {
        List<GetCategoryResponse> getCategoryResponses = categories.stream()
                .map(GetCategoryResponse::from)
                .collect(Collectors.toList());

        return new GetCategoriesResponse(getCategoryResponses);
    }

    public static GetCategoriesResponse fromProduct(List<ProductCategory> productCategories) {
        List<GetCategoryResponse> getCategoryResponses = productCategories.stream()
                .map(productCategory -> GetCategoryResponse.from(productCategory.getCategory()))
                .collect(Collectors.toList());

        return new GetCategoriesResponse(getCategoryResponses);
    }

    public GetCategoryResponse get(int index) {
        return getCategoryResponses.get(index);
    }
}
