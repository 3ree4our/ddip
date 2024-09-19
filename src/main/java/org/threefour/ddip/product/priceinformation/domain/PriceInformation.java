package org.threefour.ddip.product.priceinformation.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.threefour.ddip.product.domain.AutoDiscountRequest;
import org.threefour.ddip.product.domain.Price;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
public class PriceInformation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Convert(converter = Price.PriceConverter.class)
    @Column(nullable = false)
    private Price price;

    @Convert(converter = PriceDiscountPeriod.PriceDiscountPeriodConverter.class)
    private PriceDiscountPeriod priceDiscountPeriod;

    @Convert(converter = PriceDiscountRate.PriceDiscountRateConverter.class)
    private PriceDiscountRate priceDiscountRate;

    @Convert(converter = NextDiscountedAt.NextDiscountedAtConverter.class)
    private NextDiscountedAt nextDiscountedAt;

    @Convert(converter = MinPrice.MinPriceConverter.class)
    private MinPrice minPrice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Builder
    private PriceInformation(
            Long id, Product product, Price price, PriceDiscountPeriod priceDiscountPeriod,
            PriceDiscountRate priceDiscountRate, LocalDateTime createdAt,
            NextDiscountedAt nextDiscountedAt, MinPrice minPrice
    ) {
        this.id = id;
        this.product = product;
        this.price = price;
        this.priceDiscountPeriod = priceDiscountPeriod;
        this.priceDiscountRate = priceDiscountRate;
        this.createdAt = createdAt;
        this.nextDiscountedAt = nextDiscountedAt;
        this.minPrice = minPrice;
    }

    public static PriceInformation from(Product product, AutoDiscountRequest autoDiscountRequest) {
        return PriceInformation.builder()
                .product(product)
                .price(product.getPrice())
                .priceDiscountPeriod(PriceDiscountPeriod.of(autoDiscountRequest.getPriceDiscountPeriod()))
                .priceDiscountRate(PriceDiscountRate.of(autoDiscountRequest.getPriceDiscountRate()))
                .nextDiscountedAt(NextDiscountedAt.of(autoDiscountRequest.getFirstDiscountDate()))
                .minPrice(MinPrice.of(autoDiscountRequest.getMinPrice()))
                .build();
    }

    PriceDiscountPeriod getPriceDiscountPeriod() {
        return priceDiscountPeriod;
    }

    PriceDiscountRate getPriceDiscountRate() {
        return priceDiscountRate;
    }

    NextDiscountedAt getNextDiscountedAt() {
        return nextDiscountedAt;
    }

    MinPrice getMinPrice() {
        return minPrice;
    }
}
