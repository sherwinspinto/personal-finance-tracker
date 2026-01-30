package com.sherwin.fintrac.domain.common;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    public static Optional<UUID> convertToUUID(String value) {
        try {
            return Optional.of(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
