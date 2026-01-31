package com.sherwin.fintrac.infrastructure.inbound.model;

import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountCommandUseCaseOutput;
import com.sherwin.fintrac.infrastructure.inbound.common.dto.MoneyDto;
import java.time.LocalDateTime;

public record RegisterAccountResponse(
        String id, String email, MoneyDto initialBalance, LocalDateTime createdAt) {
    public static RegisterAccountResponse of(
            String id, String email, MoneyDto initialBalance, LocalDateTime createdAt) {
        return new RegisterAccountResponse(id, email, initialBalance, createdAt);
    }

    public static RegisterAccountResponse fromUseCaseOutput(
            RegisterAccountCommandUseCaseOutput useCaseOutput) {
        return RegisterAccountResponse.of(
                useCaseOutput.accountId(),
                useCaseOutput.email(),
                MoneyDto.of(useCaseOutput.initialBalance(), useCaseOutput.currencyCode()),
                useCaseOutput.createdAt());
    }
}
