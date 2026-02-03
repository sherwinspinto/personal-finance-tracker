package com.sherwin.fintrac.application.useCase.account.model;

import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import java.time.LocalDateTime;

public record RegisterAccountUseCaseCommand(
        String email, Long initialBalance, String currencyCode) {
    public static RegisterAccountUseCaseCommand of(
            String email, Long initialBalance, String currencyCode) {
        return new RegisterAccountUseCaseCommand(email, initialBalance, currencyCode);
    }

    public CreationResult<Account> toDomain(String accountId, LocalDateTime createdAt) {
        return Account.of(
                accountId, email, initialBalance, currencyCode, initialBalance, createdAt);
    }
}
