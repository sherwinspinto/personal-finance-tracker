package com.sherwin.fintrac.domain.common;

import java.util.Currency;
import java.util.Objects;

public class Validations {
    public static boolean isNullOrEmpty(String value) {
        return Objects.isNull(value) || value.isBlank();
    }

    public static boolean isValidCurrencyCode(String currencyCode) {
        try {
            Currency currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
