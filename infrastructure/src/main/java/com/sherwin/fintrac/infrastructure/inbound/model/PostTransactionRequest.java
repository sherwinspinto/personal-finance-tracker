package com.sherwin.fintrac.infrastructure.inbound.model;

import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionCommand;
import com.sherwin.fintrac.infrastructure.inbound.common.dto.MoneyDto;

public record PostTransactionRequest(
        String accountId, MoneyDto amount, String type, String description) {
    public PostTransactionCommand toCommand() {
        return new PostTransactionCommand(
                accountId, amount.value(), amount.currencyCode(), type, description);
    }
}
