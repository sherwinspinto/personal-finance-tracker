package com.sherwin.fintrac.domain.common.model;

import static com.sherwin.fintrac.domain.common.model.FieldError.*;
import static com.sherwin.fintrac.domain.common.model.ValidationParams.*;

import com.sherwin.fintrac.domain.common.Validations;
import com.sherwin.fintrac.domain.common.model.FieldError.NullObjectError;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public record Money(Long value, Currency currencyCode) {
    private static final FieldName VALUE = FieldName.of("value");
    private static final FieldName CURRENCY_CODE = FieldName.of("currencyCode");

    public Money {
        Objects.requireNonNull(value, "Money value cannot be null");
        Objects.requireNonNull(currencyCode, "Currency code cannot be null");
    }

    public static CreationResult<Money> of(Long value, String currencyCode, FieldName fieldName) {
        List<FieldError> validationErrors = new ArrayList<>();

        if (Validations.isNullOrEmpty(currencyCode)) {
            validationErrors.add(NullObjectError.of(CURRENCY_CODE.at(fieldName.fieldName())));
        } else if (!Validations.isValidCurrencyCode(currencyCode)) {
            validationErrors.add(
                    InvalidValue.of(
                            CURRENCY_CODE.at(fieldName.fieldName()), FieldValue.of(currencyCode)));
        }

        if (Objects.isNull(value))
            validationErrors.add(NullObjectError.of(VALUE.at(fieldName.fieldName())));

        return validationErrors.isEmpty()
                ? CreationResult.success(new Money(value, Currency.getInstance(currencyCode)))
                : CreationResult.failure(validationErrors);
    }
}
