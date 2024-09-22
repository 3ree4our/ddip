package org.threefour.ddip.product.elasticsearch.domain;

import lombok.Builder;
import lombok.Getter;
import org.threefour.ddip.image.domain.GetImageResponse;
import org.threefour.ddip.image.domain.Image;

@Getter
public class SearchProductResponse {
    private Long id;
    private String name;
    private int price;
    private String title;
    private String content;
    private String sellerNickName;
    private String categoryName;
    private GetImageResponse getImageResponse;

    @Builder
    private SearchProductResponse(
            Long id, String name, int price, String title, String content,
            String sellerNickName, String categoryName, GetImageResponse getImageResponse
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.title = title;
        this.content = content;
        this.sellerNickName = sellerNickName;
        this.categoryName = categoryName;
        this.getImageResponse = getImageResponse;
    }

    public static SearchProductResponse from(ProductDocument productDocument, Image image) {
        return SearchProductResponse.builder()
                .id(productDocument.getId())
                .name(productDocument.getName())
                .price(productDocument.getPrice())
                .title(productDocument.getTitle())
                .content(productDocument.getContent())
                .categoryName(productDocument.getThirdCategoryName())
                .getImageResponse(GetImageResponse.from(image))
                .build();
    }
}
