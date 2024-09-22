package org.threefour.ddip.product.elasticsearch.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.threefour.ddip.address.domain.Address;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.elasticsearch.util.AddressModifier;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(indexName = "products")
@Mapping(mappingPath = "elasticsearch/product-mapping.json")
@Setting(settingPath = "elasticsearch/product-setting.json")
public class ProductDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String name;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Long)
    private Long sellerId;

    @Field(type = FieldType.Keyword, ignoreAbove = 256)
    private String sellerNickName;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String city;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String district;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String roadAddress;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String schoolName;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String firstCategoryName;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String secondCategoryName;

    @Field(type = FieldType.Text, analyzer = "suggest_index_analyzer", searchAnalyzer = "suggest_search_analyzer")
    private String thirdCategoryName;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
    private LocalDateTime modifiedAt;

    @Builder
    private ProductDocument(
            Long id, String name, int price, String title, String content, Long sellerId, String sellerNickName,
            String city, String district, String roadAddress, String schoolName, String firstCategoryName, String secondCategoryName,
            String thirdCategoryName, LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.title = title;
        this.content = content;
        this.sellerId = sellerId;
        this.sellerNickName = sellerNickName;
        this.city = city;
        this.district = district;
        this.roadAddress = roadAddress;
        this.schoolName = schoolName;
        this.firstCategoryName = firstCategoryName;
        this.secondCategoryName = secondCategoryName;
        this.thirdCategoryName = thirdCategoryName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ProductDocument from(Product product) {
        Address sellerAddress = product.getSeller().getAddresses().get(0);
        String[] dividedAddress = sellerAddress.getRoadAddress().split(" ");
        StringBuilder roadAddressBuilder = new StringBuilder(dividedAddress[2]);

        if (dividedAddress.length > 3) {
            AddressModifier.joinLongRoadAddress(dividedAddress, roadAddressBuilder);
        }

        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().getValue())
                .title(product.getTitle())
                .content(product.getContent())
                .sellerId(product.getSeller().getId())
                .sellerNickName(product.getSeller().getNickName())
                .city(dividedAddress[0])
                .district(dividedAddress[1])
                .roadAddress(roadAddressBuilder.toString())
                .schoolName(product.getSeller().getSchool())
                .firstCategoryName(product.getProductCategories().get(0).getCategory().getName().getDescription())
                .secondCategoryName(
                        product.getProductCategories().size() > 1
                                ? product.getProductCategories().get(1).getCategory().getName().getDescription()
                                : null
                )
                .thirdCategoryName(
                        product.getProductCategories().size() > 2
                                ? product.getProductCategories().get(2).getCategory().getName().getDescription()
                                : null
                )
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProductDocument that = (ProductDocument) object;
        return price == that.price && Objects.equals(
                id, that.id) && Objects.equals(name, that.name) && Objects.equals(title, that.title)
                && Objects.equals(content, that.content) && Objects.equals(sellerId, that.sellerId)
                && Objects.equals(sellerNickName, that.sellerNickName) && Objects.equals(city, that.city)
                && Objects.equals(district, that.district) && Objects.equals(roadAddress, that.roadAddress)
                && Objects.equals(schoolName, that.schoolName)
                && Objects.equals(firstCategoryName, that.firstCategoryName)
                && Objects.equals(secondCategoryName, that.secondCategoryName)
                && Objects.equals(thirdCategoryName, that.thirdCategoryName)
                && Objects.equals(createdAt, that.createdAt) && Objects.equals(modifiedAt, that.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, price, title, content, sellerId, sellerNickName, city, district, roadAddress,
                schoolName, firstCategoryName, secondCategoryName, thirdCategoryName, createdAt, modifiedAt
        );
    }
}
