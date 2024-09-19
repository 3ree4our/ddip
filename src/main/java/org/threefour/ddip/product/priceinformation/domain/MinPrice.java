package org.threefour.ddip.product.priceinformation.domain;

import org.threefour.ddip.product.exception.InvalidPriceException;
import org.threefour.ddip.product.exception.PriceNoValueException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.INVALID_MIN_PRICE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.MIN_PRICE_NO_VALUE_EXCEPTION_MESSAGE;


public class MinPrice {
    private final int minPrice;

    private MinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public static MinPrice of(String minPrice) {
        validate(minPrice);
        return new MinPrice(FormatConverter.parseToInt(minPrice));
    }

    private static void validate(String minPrice) {
        checkMinPriceHasValue(minPrice);
        checkMinPricePattern(minPrice);
    }

    private static void checkMinPriceHasValue(String minPrice) {
        if (!FormatValidator.hasValue(minPrice)) {
            throw new PriceNoValueException(MIN_PRICE_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkMinPricePattern(String minPrice) {
        if (!FormatValidator.isPositiveNumberOrZeroPattern(minPrice)) {
            throw new InvalidPriceException(String.format(INVALID_MIN_PRICE_EXCEPTION_MESSAGE, minPrice));
        }
    }

    int getValue() {
        return minPrice;
    }

    @Converter
    public static class MinPriceConverter implements AttributeConverter<MinPrice, Integer> {
        @Override
        public Integer convertToDatabaseColumn(MinPrice minPrice) {
            return minPrice.minPrice;
        }

        @Override
        public MinPrice convertToEntityAttribute(Integer minPrice) {
            return minPrice == null ? null : new MinPrice(minPrice);
        }
    }
}
