package org.threefour.ddip.product.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.threefour.ddip.product.exception.InvalidPriceException;
import org.threefour.ddip.product.exception.PriceNoValueException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;

import static org.threefour.ddip.product.exception.ExceptionMessage.INVALID_PRICE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.product.exception.ExceptionMessage.PRICE_NO_VALUE_EXCEPTION_MESSAGE;

public class Price implements Serializable {
    private final int price;

    private Price(int price) {
        this.price = price;
    }

    @JsonCreator
    public static Price of(@JsonProperty("price") String price) {
        validate(price);
        return new Price(FormatConverter.parseToInt(price));
    }

    private static void validate(String price) {
        checkPriceHasValue(price);
        checkPricePattern(price);
    }

    private static void checkPriceHasValue(String price) {
        if (!FormatValidator.hasValue(price)) {
            throw new PriceNoValueException(PRICE_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkPricePattern(String price) {
        if (!FormatValidator.isPositiveNumberPattern(price)) {
            throw new InvalidPriceException(String.format(INVALID_PRICE_EXCEPTION_MESSAGE, price));
        }
    }

    @JsonProperty(value = "price")
    int getValue() {
        return price;
    }

    @Converter
    public static class PriceConverter implements AttributeConverter<Price, Integer> {
        @Override
        public Integer convertToDatabaseColumn(Price price) {
            return price.price;
        }

        @Override
        public Price convertToEntityAttribute(Integer price) {
            return price == null ? null : new Price(price);
        }
    }
}
