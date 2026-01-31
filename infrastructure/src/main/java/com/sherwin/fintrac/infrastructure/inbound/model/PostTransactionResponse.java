package com.sherwin.fintrac.infrastructure.inbound.model;

import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.infrastructure.inbound.common.dto.MoneyDto;
import java.time.LocalDateTime;

public record PostTransactionResponse(
        String transactionId,
        String accountId,
        String type,
        String description,
        MoneyDto amount,
        LocalDateTime createdAt) {
    public static PostTransactionResponse of(
            String transactionId,
            String accountId,
            String type,
            String description,
            MoneyDto amount,
            LocalDateTime createdAt) {
        return new PostTransactionResponse(
                transactionId, accountId, type, description, amount, createdAt);
    }

    public static PostTransactionResponse fromUseCase(
            PostTransactionUseCaseResponse useCaseResponse) {
        return new PostTransactionResponse(
                useCaseResponse.transactionId(),
                useCaseResponse.accountId(),
                useCaseResponse.type(),
                useCaseResponse.description(),
                MoneyDto.of(useCaseResponse.amount(), useCaseResponse.currencyCode()),
                useCaseResponse.createdAt());
    }
}
