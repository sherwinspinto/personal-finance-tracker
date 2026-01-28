package com.sherwin.fintrac.application.useCase.model;

import java.util.UUID;

public record PostTransactionCommand(
        UUID accountId, long amount, String currencyCode, String type, String description) {}
