package com.sherwin.fintrac.application.useCase.account.model;

import com.sherwin.fintrac.domain.account.Account;
import java.time.LocalDateTime;

public record RegisterAccountCommandUseCaseOutput(
        String accountId,
        Long initialBalance,
        String currencyCode,
        String email,
        LocalDateTime createdAt) {
    public static RegisterAccountCommandUseCaseOutput fromDomain(Account createdAccount) {
        return new RegisterAccountCommandUseCaseOutput(
                createdAccount.id().value().toString(),
                createdAccount.initialBalance().value(),
                createdAccount.initialBalance().currencyCode().getCurrencyCode(),
                createdAccount.email().value(),
                createdAccount.createdAt().value());
    }
}
