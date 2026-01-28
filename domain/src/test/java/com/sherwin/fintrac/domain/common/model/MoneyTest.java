package com.sherwin.fintrac.domain.common.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;
import org.junit.jupiter.api.Test;

class MoneyTest {
    private static final FieldName FIELD_NAME = FieldName.of("balance");

    @Test
    void test_money_creation() {
        Long amount = 100L;
        String currencyCode = "USD";
        Currency currency = Currency.getInstance(currencyCode);
        CreationResult<Money> balance = Money.of(amount, currencyCode, FIELD_NAME);
        switch (balance) {
            case CreationResult.Success<Money> success -> {
                assertEquals(amount, success.value().value());
                assertEquals(currency, success.value().currencyCode());
            }
            case CreationResult.Failure<Money> failure -> fail("Expected success, got failure");
        }
    }

    @Test
    void test_money_creation_failure() {
        Long amount = 100L;
        String currencyCode = "INVALID_CURRENCY_CODE";
        CreationResult<Money> balance = Money.of(amount, currencyCode, FIELD_NAME);
        switch (balance) {
            case CreationResult.Success<Money> success -> fail("Expected failure, got success");
            case CreationResult.Failure<Money> failure -> {
                assertEquals(1, failure.validationErrors().size());
                assertEquals(-101, failure.validationErrors().getFirst().errorCode());
                assertEquals("INVALID_VALUE", failure.validationErrors().getFirst().errorMessage());
            }
        }
    }
}
