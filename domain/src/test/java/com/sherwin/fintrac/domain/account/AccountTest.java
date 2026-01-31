package com.sherwin.fintrac.domain.account;

import static org.junit.jupiter.api.Assertions.*;

import com.sherwin.fintrac.domain.common.model.CreationResult;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AccountTest {
    @Test
    void test_account_creation() {
        String accountIdString = UUID.randomUUID().toString();
        Long amount = 2L;
        String currencyCode = "USD";
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 10);
        String email = "test@fintractester.com";

        CreationResult<Account> creationResult =
                Account.of(accountIdString, email, amount, currencyCode, createdAt);
        assertInstanceOf(CreationResult.Success.class, creationResult);
    }

    @Test
    void test_account_creation_failure() {
        String accountIdString = null;
        Long amount = 0L;
        String currencyCode = "INVALID_CURRENCY_CODE";
        LocalDateTime createdAt = null;
        String email = "testfintractester.com";

        CreationResult<Account> creationResult =
                Account.of(accountIdString, email, amount, currencyCode, createdAt);
        IO.println(creationResult);
        assertInstanceOf(CreationResult.Failure.class, creationResult);
    }
}
