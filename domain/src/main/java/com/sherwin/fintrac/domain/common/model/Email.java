package com.sherwin.fintrac.domain.common.model;

import com.sherwin.fintrac.domain.common.Validations;
import java.util.List;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public Email {
        if (value == null) {
            throw new IllegalArgumentException("Email value cannot be null");
        }
    }

    public static CreationResult<Email> of(String value, FieldName fieldName) {
        if (Validations.isNullOrEmpty(value)) {
            return CreationResult.failure(List.of(FieldError.EmptyStringError.of(fieldName)));
        } else if (!EMAIL_PATTERN.matcher(value).matches()) {
            return CreationResult.failure(
                    List.of(
                            FieldError.InvalidValue.of(
                                    fieldName, ValidationParams.FieldValue.of(value))));
        }
        return CreationResult.success(new Email(value));
    }
}
