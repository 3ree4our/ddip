package org.threefour.ddip.product.domain;

import lombok.Builder;
import lombok.Getter;
import org.threefour.ddip.image.domain.GetImageResponse;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;
import org.threefour.ddip.product.elasticsearch.util.AddressModifier;
import org.threefour.ddip.product.priceinformation.domain.GetPriceInformationListResponse;
import org.threefour.ddip.product.priceinformation.domain.PriceInformation;
import org.threefour.ddip.util.FormatValidator;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetProductResponse implements Serializable {
    private static final long serialVersionUID = 2L;

    private Long id;
    private String name;
    private int price;
    private String title;
    private String content;
    private Long sellerId;
    private String sellerNickName;
    private String sellerRegion;
    private String schoolName;
    private GetCategoriesResponse getCategoriesResponse;
    private GetPriceInformationListResponse getPriceInformationListResponse;
    private List<GetImageResponse> imageResponses;
    private int waitingNumber;

    @Builder
    private GetProductResponse(
            Long id, String name, int price, String title, String content, Long sellerId,
            String sellerNickName, String sellerRegion, String schoolName, GetCategoriesResponse getCategoriesResponse,
            GetPriceInformationListResponse getPriceInformationListResponse,
            List<GetImageResponse> imageResponses, int waitingNumber
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.title = title;
        this.content = content;
        this.sellerId = sellerId;
        this.sellerNickName = sellerNickName;
        this.sellerRegion = sellerRegion;
        this.schoolName = schoolName;
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
                .sellerId(product.getSeller().getId())
                .sellerNickName(product.getSeller().getNickName())
                .sellerRegion(product.getSeller().getAddresses().get(0).getRoadAddress())
                .schoolName(product.getSeller().getSchool())
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
                .sellerId(product.getSeller().getId())
                .sellerNickName(product.getSeller().getNickName())
                .sellerRegion(product.getSeller().getAddresses().get(0).getRoadAddress())
                .schoolName(product.getSeller().getSchool())
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
                .sellerId(product.getSeller().getId())
                .sellerNickName(product.getSeller().getNickName())
                .sellerRegion(product.getSeller().getAddresses().get(0).getRoadAddress())
                .schoolName(product.getSeller().getSchool())
                .getCategoriesResponse(GetCategoriesResponse.fromProduct(product.getProductCategories()))
                .imageResponses(images.stream().map(GetImageResponse::from).collect(Collectors.toList()))
                .waitingNumber(waitingNumber)
                .build();
    }

    public static GetProductResponse from(ProductDocument productDocument, List<Image> images) {
        return GetProductResponse.builder()
                .id(productDocument.getId())
                .name(productDocument.getName())
                .price(productDocument.getPrice())
                .title(productDocument.getTitle())
                .content(productDocument.getContent())
                .sellerId(productDocument.getSellerId())
                .sellerNickName(productDocument.getSellerNickName())
                .sellerRegion(AddressModifier.joinAddress(productDocument))
                .schoolName(productDocument.getSchoolName())
                .getCategoriesResponse(GetCategoriesResponse.fromProductDocument(productDocument))
                .imageResponses(
                        FormatValidator.hasValue(images)
                                ? images.stream().map(GetImageResponse::from).collect(Collectors.toList())
                                : null
                )
                .build();
    }

    public GetImageResponse getImage(int index) {
        return imageResponses.get(index);
    }
}
