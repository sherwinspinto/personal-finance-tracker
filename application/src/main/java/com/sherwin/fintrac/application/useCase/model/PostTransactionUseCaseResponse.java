package com.sherwin.fintrac.application.useCase.model;

import com.sherwin.fintrac.domain.transaction.Transaction;
import java.util.UUID;

public record PostTransactionUseCaseResponse(
        UUID accountId,
        UUID transactionId,
        long amount,
        String currency,
        String description,
        String type) {
    public static PostTransactionUseCaseResponse fromDomain(Transaction transaction) {
        return new PostTransactionUseCaseResponse(
                transaction.accountId().value(),
                transaction.id().value(),
                transaction.amount().value(),
                transaction.amount().currencyCode().getSymbol(),
                transaction.description().value(),
                transaction.type().name());
    }
}
