package com.sherwin.fintrac.domain.common.model;

import com.sherwin.fintrac.domain.common.Validations;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record AccountId(UUID value) {
    private static final FieldName FIELD_NAME = FieldName.of("accountId");

    public AccountId {
        Objects.requireNonNull(value, "Account ID cannot be null");
    }

    public static CreationResult<AccountId> of(UUID value) {
        return CreationResult.success(new AccountId(value));
    }

    public static CreationResult<AccountId> of(String value) {
        if (Validations.isNullOrEmpty(value))
            return CreationResult.failure(List.of(new FieldError.EmptyStringError(FIELD_NAME)));
        Optional<UUID> uuid = Validations.convertToUUID(value);
        if (uuid.isPresent()) return CreationResult.success(new AccountId(uuid.get()));
        return CreationResult.failure(
                List.of(
                        new FieldError.InvalidValue<>(
                                FIELD_NAME, ValidationParams.FieldValue.of(value))));
    }
}
