package com.sherwin.fintrac.infrastructure.inbound.model;

import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountUseCaseCommand;
import com.sherwin.fintrac.infrastructure.inbound.common.dto.MoneyDto;

public record RegisterAccountRequest(String email, MoneyDto initialBalance) {
    public static RegisterAccountRequest of(String email, MoneyDto initialBalance) {
        return new RegisterAccountRequest(email, initialBalance);
    }

    public RegisterAccountUseCaseCommand toCommand() {
        return RegisterAccountUseCaseCommand.of(
                email, initialBalance.value(), initialBalance.currencyCode());
    }
}
