package org.threefour.ddip.product.priceinformation.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.threefour.ddip.product.priceinformation.exception.InvalidNextDiscountedAtException;
import org.threefour.ddip.product.priceinformation.exception.NextDiscountedAtNoValueException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.INVALID_NEXT_DISCOUNTED_AT_EXCEPTION_MESSAGE;
import static org.threefour.ddip.product.priceinformation.exception.ExceptionMessage.NEXT_DISCOUNTED_AT_NO_VALUE_EXCEPTION_MESSAGE;

public class NextDiscountedAt {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final Timestamp nextDiscountedAt;

    private NextDiscountedAt(Timestamp nextDiscountedAt) {
        this.nextDiscountedAt = nextDiscountedAt;
    }

    public static NextDiscountedAt of(String nextDiscountedAt) {
        validate(nextDiscountedAt);
        return new NextDiscountedAt(FormatConverter.parseToDate(nextDiscountedAt));
    }

    private static void validate(String nextDiscountedAt) {
        checkDateHasValue(nextDiscountedAt);
        checkDatePattern(nextDiscountedAt);
    }

    private static void checkDateHasValue(String nextDiscountedAt) {
        if (FormatValidator.isNoValue(nextDiscountedAt)) {
            throw new NextDiscountedAtNoValueException(NEXT_DISCOUNTED_AT_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkDatePattern(String nextDiscountedAt) {
        if (!FormatValidator.isDatePattern(nextDiscountedAt)) {
            throw new InvalidNextDiscountedAtException(
                    String.format(INVALID_NEXT_DISCOUNTED_AT_EXCEPTION_MESSAGE, nextDiscountedAt)
            );
        }
    }

    @Converter
    public static class NextDiscountedAtConverter implements AttributeConverter<NextDiscountedAt, Timestamp> {
        @Override
        public Timestamp convertToDatabaseColumn(NextDiscountedAt nextDiscountedAt) {
            return nextDiscountedAt.nextDiscountedAt;
        }

        @Override
        public NextDiscountedAt convertToEntityAttribute(Timestamp nextDiscountedAt) {
            return nextDiscountedAt == null ? null : new NextDiscountedAt(nextDiscountedAt);
        }
    }

    Timestamp getValue() {
        return nextDiscountedAt;
    }
}
