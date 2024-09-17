package org.threefour.ddip.product.priceinformation.domain;

import org.threefour.ddip.product.priceinformation.exception.InvalidPriceDiscountPeriodException;
import org.threefour.ddip.product.priceinformation.exception.PriceDiscountPeriodNoValueException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.INVALID_PRICE_DISCOUNT_PERIOD_EXCEPTION_MESSAGE;
import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.PRICE_DISCOUNT_PERIOD_NO_VALUE_EXCEPTION_MESSAGE;

public class PriceDiscountPeriod {
    private final short priceDiscountPeriod;

    private PriceDiscountPeriod(short priceDiscountPeriod) {
        this.priceDiscountPeriod = priceDiscountPeriod;
    }

    public static PriceDiscountPeriod of(String priceDiscountPeriod) {
        validate(priceDiscountPeriod);
        return new PriceDiscountPeriod(FormatConverter.parseToShort(priceDiscountPeriod));
    }

    private static void validate(String priceDiscountPeriod) {
        checkPeriodHasValue(priceDiscountPeriod);
        checkPeriodPattern(priceDiscountPeriod);
    }

    private static void checkPeriodHasValue(String priceDiscountPeriod) {
        if (FormatValidator.isNoValue(priceDiscountPeriod)) {
            throw new PriceDiscountPeriodNoValueException(PRICE_DISCOUNT_PERIOD_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkPeriodPattern(String priceDiscountPeriod) {
        if (!FormatValidator.isNumberPattern(priceDiscountPeriod)) {
            throw new InvalidPriceDiscountPeriodException(
                    String.format(INVALID_PRICE_DISCOUNT_PERIOD_EXCEPTION_MESSAGE, priceDiscountPeriod)
            );
        }
    }

    @Converter
    public static class PriceDiscountPeriodConverter implements AttributeConverter<PriceDiscountPeriod, Short> {
        @Override
        public Short convertToDatabaseColumn(PriceDiscountPeriod priceDiscountPeriod) {
            return priceDiscountPeriod.priceDiscountPeriod;
        }

        @Override
        public PriceDiscountPeriod convertToEntityAttribute(Short priceDiscountPeriod) {
            return priceDiscountPeriod == null ? null : new PriceDiscountPeriod(priceDiscountPeriod);
        }
    }

    short getValue() {
        return priceDiscountPeriod;
    }
}
