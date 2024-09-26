package org.threefour.ddip.product.priceinformation.domain;

import org.threefour.ddip.product.priceinformation.exception.InvalidPriceDiscountRateException;
import org.threefour.ddip.product.priceinformation.exception.PriceDiscountRateNoValueException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.INVALID_PRICE_DISCOUNT_RATE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.PRICE_DISCOUNT_RATE_NO_VALUE_EXCEPTION_MESSAGE;

public class PriceDiscountRate {
    private final float priceDiscountRate;

    private PriceDiscountRate(float priceDiscountRate) {
        this.priceDiscountRate = priceDiscountRate;
    }

    public static PriceDiscountRate of(String priceDiscountPercentRate) {
        validate(priceDiscountPercentRate);
        return new PriceDiscountRate(FormatConverter.parseToFloat(priceDiscountPercentRate) / 100);
    }

    private static void validate(String priceDiscountPercentRate) {
        checkRateHasValue(priceDiscountPercentRate);
        checkRatePattern(priceDiscountPercentRate);
    }

    private static void checkRateHasValue(String priceDiscountPercentRate) {
        if (!FormatValidator.hasValue(priceDiscountPercentRate)) {
            throw new PriceDiscountRateNoValueException(PRICE_DISCOUNT_RATE_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkRatePattern(String priceDiscountPercentRate) {
        if (!FormatValidator.isPositiveNumberPattern(priceDiscountPercentRate)) {
            throw new InvalidPriceDiscountRateException(
                    String.format(INVALID_PRICE_DISCOUNT_RATE_EXCEPTION_MESSAGE, priceDiscountPercentRate)
            );
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PriceDiscountRate that = (PriceDiscountRate) object;
        return Float.compare(priceDiscountRate, that.priceDiscountRate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priceDiscountRate);
    }

    @Converter
    public static class PriceDiscountRateConverter implements AttributeConverter<PriceDiscountRate, Float> {
        @Override
        public Float convertToDatabaseColumn(PriceDiscountRate priceDiscountRate) {
            return priceDiscountRate.priceDiscountRate;
        }

        @Override
        public PriceDiscountRate convertToEntityAttribute(Float priceDiscountRate) {
            return priceDiscountRate == null ? null : new PriceDiscountRate(priceDiscountRate);
        }
    }

    byte getPercentValue() {
        return (byte) (priceDiscountRate * 100);
    }
}
