package org.threefour.ddip.deal.domain;

import org.threefour.ddip.deal.exception.DealAmountNoValueException;
import org.threefour.ddip.deal.exception.InvalidDealAmountException;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static org.threefour.ddip.deal.exception.ExceptionMessage.DEAL_AMOUNT_NO_VALUE_EXCEPTION_MESSAGE;
import static org.threefour.ddip.deal.exception.ExceptionMessage.INVALID_DEAL_AMOUNT_EXCEPTION_MESSAGE;

public class DealAmount {
    private final int dealAmount;

    private DealAmount(int dealAmount) {
        this.dealAmount = dealAmount;
    }

    public static DealAmount of(String dealAmount) {
        validate(dealAmount);
        return new DealAmount(FormatConverter.parseToInt(dealAmount));
    }

    private static void validate(String dealAmount) {
        checkDealAmountHasValue(dealAmount);
        checkDealAmountPattern(dealAmount);
    }

    private static void checkDealAmountHasValue(String dealAmount) {
        if (!FormatValidator.hasValue(dealAmount)) {
            throw new DealAmountNoValueException(DEAL_AMOUNT_NO_VALUE_EXCEPTION_MESSAGE);
        }
    }

    private static void checkDealAmountPattern(String dealAmount) {
        if (!FormatValidator.isPositiveNumberPattern(dealAmount)) {
            throw new InvalidDealAmountException(String.format(INVALID_DEAL_AMOUNT_EXCEPTION_MESSAGE, dealAmount));
        }
    }

    int getValue() {
        return dealAmount;
    }

    @Converter
    public static class DealAmountConverter implements AttributeConverter<DealAmount, Integer> {
        @Override
        public Integer convertToDatabaseColumn(DealAmount dealAmount) {
            return dealAmount.dealAmount;
        }

        @Override
        public DealAmount convertToEntityAttribute(Integer dealAmount) {
            return dealAmount == null ? null : new DealAmount(dealAmount);
        }
    }
}
