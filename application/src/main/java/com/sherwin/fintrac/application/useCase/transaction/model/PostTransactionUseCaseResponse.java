package com.sherwin.fintrac.application.useCase.transaction.model;

import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.LocalDateTime;

public record PostTransactionUseCaseResponse(
        String accountId,
        String transactionId,
        long amount,
        String currencyCode,
        String description,
        String type,
        LocalDateTime createdAt) {
    public static PostTransactionUseCaseResponse fromDomain(Transaction transaction) {
        return new PostTransactionUseCaseResponse(
                transaction.accountId().value().toString(),
                transaction.id().value().toString(),
                transaction.amount().value(),
                transaction.amount().currencyCode().getCurrencyCode(),
                transaction.description().value(),
                transaction.type().name(),
                transaction.createdAt().value());
    }
}
