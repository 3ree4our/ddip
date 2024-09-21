package org.threefour.ddip.product.domain;

import lombok.Builder;
import lombok.Getter;
import org.threefour.ddip.image.domain.GetImageResponse;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.priceinformation.domain.GetPriceInformationListResponse;
import org.threefour.ddip.product.priceinformation.domain.PriceInformation;
import org.threefour.ddip.util.FormatValidator;

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
    private int waitingNumber;

    @Builder
    private GetProductResponse(
            Long id, String name, int price, String title, String content,
            Member seller, GetCategoriesResponse getCategoriesResponse,
            GetPriceInformationListResponse getPriceInformationListResponse,
            List<GetImageResponse> imageResponses, int waitingNumber
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
        this.waitingNumber = waitingNumber;
    }

    public static GetProductResponse from(Product product) {
        return GetProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().getValue())
                .title(product.getTitle())
                .content(product.getContent())
                .seller(product.getSeller())
                .getCategoriesResponse(GetCategoriesResponse.fromProduct(product.getProductCategories()))
                .build();
    }

    public static GetProductResponse from(Product product, List<Image> images) {
        List<PriceInformation> priceInformation = product.getPriceInformation();

        return GetProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().getValue())
                .title(product.getTitle())
                .content(product.getContent())
                .seller(product.getSeller())
                .getCategoriesResponse(GetCategoriesResponse.fromProduct(product.getProductCategories()))
                .getPriceInformationListResponse(
                        FormatValidator.hasValue(priceInformation)
                                ? GetPriceInformationListResponse.from(priceInformation)
                                : null
                )
                .imageResponses(
                        FormatValidator.hasValue(images)
                                ? images.stream().map(GetImageResponse::from).collect(Collectors.toList())
                                : null
                )
                .build();
    }

    public static GetProductResponse from(Product product, List<Image> images, int waitingNumber) {
        return GetProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().getValue())
                .title(product.getTitle())
                .content(product.getContent())
                .seller(product.getSeller())
                .getCategoriesResponse(GetCategoriesResponse.fromProduct(product.getProductCategories()))
                .imageResponses(images.stream().map(GetImageResponse::from).collect(Collectors.toList()))
                .waitingNumber(waitingNumber)
                .build();
    }

    public GetImageResponse getImage(int index) {
        return imageResponses.get(index);
    }
}
