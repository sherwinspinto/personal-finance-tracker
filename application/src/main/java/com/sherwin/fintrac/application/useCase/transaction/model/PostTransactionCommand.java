package com.sherwin.fintrac.application.useCase.transaction.model;

public record PostTransactionCommand(
        String accountId, long amount, String currencyCode, String type, String description) {}
