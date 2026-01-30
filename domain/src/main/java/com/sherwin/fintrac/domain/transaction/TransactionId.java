package com.sherwin.fintrac.domain.transaction;

import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.domain.common.model.FieldName;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record TransactionId(UUID value) {
    private static final FieldName FIELD_NAME = FieldName.of("transactionId");

    public TransactionId {
        Objects.requireNonNull(value, "Transaction ID cannot be null");
    }

    public static CreationResult<TransactionId> of(UUID value) {
        return Objects.isNull(value)
                ? CreationResult.failure(List.of(new FieldError.NullObjectError(FIELD_NAME)))
                : CreationResult.success(new TransactionId(value));
    }
}
