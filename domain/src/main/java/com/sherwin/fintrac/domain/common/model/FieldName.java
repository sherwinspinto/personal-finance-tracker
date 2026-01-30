package com.sherwin.fintrac.domain.common.model;

import java.util.Objects;

public record FieldName(String fieldName) {
    public FieldName {
        if (Objects.isNull(fieldName) || fieldName.isBlank()) {
            throw new IllegalArgumentException("Field name cannot be null or blank");
        }
    }

    public FieldName at(String root) {
        return FieldName.of(root + "." + this.fieldName);
    }

    public static FieldName of(String fieldName) {
        return new FieldName(fieldName);
    }
}
