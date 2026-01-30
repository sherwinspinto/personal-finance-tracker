package com.sherwin.fintrac.infrastructure.inbound.common.dto;

public record MoneyDto(Long value, String currencyCode) {
    public static MoneyDto of(Long value, String currencyCode) {
        return new MoneyDto(value, currencyCode);
    }
}
