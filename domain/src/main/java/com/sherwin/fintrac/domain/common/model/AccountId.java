package com.sherwin.fintrac.domain.common.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {
    private static final FieldName FIELD_NAME = FieldName.of("accountId");

    public AccountId {
        Objects.requireNonNull(value, "Account ID cannot be null");
    }

    public static CreationResult<AccountId> of(UUID value) {
        return Objects.isNull(value)
                ? CreationResult.failure(List.of(FieldError.NullObjectError.of(FIELD_NAME)))
                : CreationResult.success(new AccountId(value));
    }
}
