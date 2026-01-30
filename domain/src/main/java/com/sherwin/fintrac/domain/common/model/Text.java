package com.sherwin.fintrac.domain.common.model;

import com.sherwin.fintrac.domain.common.Validations;
import com.sherwin.fintrac.domain.common.model.FieldError.EmptyStringError;
import com.sherwin.fintrac.domain.common.model.FieldError.FieldErrorWithParams.GreaterThanMaxError;
import com.sherwin.fintrac.domain.common.model.ValidationParams.Param;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Text(String value) {
    public Text {
        Objects.requireNonNull(value, "Description cannot be null");
    }

    public static CreationResult<Text> of(String value, FieldName fieldName, int maxLength) {
        List<FieldError> validationErrors = new ArrayList<>();

        if (Validations.isNullOrEmpty(value)) {
            validationErrors.add(EmptyStringError.of(fieldName));
        } else if (value.length() > maxLength) {
            validationErrors.add(
                    GreaterThanMaxError.of(
                            fieldName,
                            ValidationParams.FieldValue.of(value.length()),
                            Param.of(maxLength)));
        }

        return validationErrors.isEmpty()
                ? CreationResult.success(new Text(value))
                : CreationResult.failure(validationErrors);
    }
}
