package com.sherwin.fintrac.domain.common.model;

import static com.sherwin.fintrac.domain.common.model.FieldError.*;
import static com.sherwin.fintrac.domain.common.model.ValidationParams.*;

import com.sherwin.fintrac.domain.common.Validations;
import com.sherwin.fintrac.domain.common.model.FieldError.NullObjectError;
import java.util.*;
import java.util.function.Function;

public record Money(Long value, Currency currencyCode) {
    public static final FieldName VALUE = FieldName.of("value");
    public static final FieldName CURRENCY_CODE = FieldName.of("currencyCode");

    public Money {
        Objects.requireNonNull(value, "Money value cannot be null");
        Objects.requireNonNull(currencyCode, "Currency code cannot be null");
    }

    public static CreationResult<Money> of(Long value, String currencyCode, FieldName fieldName) {
        return of(value, currencyCode, fieldName, null);
    }

    public static CreationResult<Money> of(
            Long value, String currencyCode, FieldName fieldName, MoneyParamsValidator validator) {
        List<FieldError> baseValidationErrors = baseValidation(value, currencyCode, fieldName);
        List<FieldError> customValidationErrors =
                customValidation(value, currencyCode, fieldName, validator);
        List<FieldError> validationErrors = new ArrayList<>(baseValidationErrors);
        validationErrors.addAll(customValidationErrors);

        return validationErrors.isEmpty()
                ? CreationResult.success(new Money(value, Currency.getInstance(currencyCode)))
                : CreationResult.failure(List.copyOf(validationErrors));
    }

    static List<FieldError> customValidation(
            Long value, String currencyCode, FieldName fieldName, MoneyParamsValidator validator) {
        return Optional.ofNullable(validator)
                .map(applyCustomValidator(value, currencyCode, fieldName))
                .filter(errors -> !errors.isEmpty())
                .orElse(Collections.emptyList());
    }

    static Function<MoneyParamsValidator, List<FieldError>> applyCustomValidator(
            Long value, String currencyCode, FieldName fieldName) {
        return moneyParamsValidator ->
                moneyParamsValidator.validate(value, currencyCode, fieldName);
    }

    static List<FieldError> baseValidation(Long value, String currencyCode, FieldName fieldName) {
        List<FieldError> validationErrors = new ArrayList<>();

        if (Validations.isNullOrEmpty(currencyCode)) {
            validationErrors.add(NullObjectError.of(CURRENCY_CODE.at(fieldName.fieldName())));
        } else if (!Validations.isValidCurrencyCode(currencyCode)) {
            validationErrors.add(
                    InvalidValue.of(
                            CURRENCY_CODE.at(fieldName.fieldName()), FieldValue.of(currencyCode)));
        }
        return validationErrors;
    }

    @FunctionalInterface
    public interface MoneyParamsValidator {
        List<FieldError> validate(Long value, String currencyCode, FieldName fieldName);
    }
}
