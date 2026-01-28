package com.sherwin.fintrac.domain.common.model;

import com.sherwin.fintrac.domain.common.model.FieldError.NullObjectError;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record CreatedAt(LocalDateTime value) {
    public CreatedAt {
        Objects.requireNonNull(value, "Created at cannot be null");
    }

    public static CreationResult<CreatedAt> of(LocalDateTime value, FieldName fieldName) {
        return Objects.isNull(value)
                ? CreationResult.failure(List.of(NullObjectError.of(fieldName)))
                : CreationResult.success(new CreatedAt(value));
    }
}
