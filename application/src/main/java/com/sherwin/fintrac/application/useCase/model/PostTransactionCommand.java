package com.sherwin.fintrac.application.useCase.model;

public record PostTransactionCommand(
        String accountId, long amount, String currencyCode, String type, String description) {}
