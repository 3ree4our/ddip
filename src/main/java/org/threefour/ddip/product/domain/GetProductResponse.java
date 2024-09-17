package org.threefour.ddip.product.domain;

import lombok.Builder;
import lombok.Getter;
import org.threefour.ddip.image.domain.GetImageResponse;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.priceinformation.domain.GetPriceInformationListResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetProductResponse {
    private Long id;
    private String name;
    private int price;
    private String title;
    private String content;
    private Member seller;
    private GetCategoriesResponse getCategoriesResponse;
    private GetPriceInformationListResponse getPriceInformationListResponse;
    private List<GetImageResponse> imageResponses;

    @Builder
    private GetProductResponse(
            Long id, String name, int price, String title, String content,
            Member seller, GetCategoriesResponse getCategoriesResponse,
            GetPriceInformationListResponse getPriceInformationListResponse, List<GetImageResponse> imageResponses
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.title = title;
        this.content = content;
        this.seller = seller;
        this.getCategoriesResponse = getCategoriesResponse;
        this.getPriceInformationListResponse = getPriceInformationListResponse;
        this.imageResponses = imageResponses;
    }

    public static GetProductResponse from(Product product, List<Image> images) {
        return GetProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().getValue())
                .title(product.getTitle())
                .content(product.getContent())
                .seller(product.getSeller())
                .getCategoriesResponse(GetCategoriesResponse.fromProduct(product.getProductCategories()))
                .getPriceInformationListResponse(GetPriceInformationListResponse.from(product.getPriceInformation()))
                .imageResponses(images.stream().map(GetImageResponse::from).collect(Collectors.toList()))
                .build();
    }

    public GetImageResponse getImage(int index) {
        return imageResponses.get(index);
    }
}
