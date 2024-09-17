package org.threefour.ddip.product.domain;

import org.threefour.ddip.product.exception.InvalidPriceException;
import org.threefour.ddip.product.exception.PriceNoValueException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.threefour.ddip.product.exception.ExceptionMessage.INVALID_PRICE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.product.exception.ExceptionMessage.PRICE_NO_VALUE_EXCEPTION_MESSAGE;

public class Price {
    private final int price;

    private Price(int price) {
        this.price = price;
    }

    public static Price of(String price) {
        validate(price);
        return new Price(FormatConverter.parseToInt(price));
    }

    private static void validate(String price) {
        checkPriceHasValue(price);
        checkPricePattern(price);
    }

    private static void checkPriceHasValue(String price) {
        if (FormatValidator.isNoValue(price)) {
            throw new PriceNoValueException(PRICE_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkPricePattern(String price) {
        if (!FormatValidator.isNumberPattern(price)) {
            throw new InvalidPriceException(String.format(INVALID_PRICE_EXCEPTION_MESSAGE, price));
        }
    }

    public static Price from(Product product) {
        return new Price(product.getPrice().getValue());
    }

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