package com.sherwin.fintrac.infrastructure.inbound.common.dto;

import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.domain.common.model.FieldError.*;
import com.sherwin.fintrac.domain.common.model.FieldError.FieldErrorWithParams.*;
import java.util.Map;

public record Error(int errorCode, String errorMessage, String field, Map<String, Object> params) {
    public static Error fromDomain(FieldError fieldError) {
        return switch (fieldError) {
            case NullObjectError nullObjectError ->
                    new Error(
                            nullObjectError.errorCode(),
                            nullObjectError.errorMessage(),
                            nullObjectError.fieldName().fieldName(),
                            Map.of());
            case InvalidValue<?> invalidValue ->
                    new Error(
                            invalidValue.errorCode(),
                            invalidValue.errorMessage(),
                            invalidValue.fieldName().fieldName(),
                            Map.of("value", invalidValue.fieldValue().value()));
            case EmptyStringError emptyStringError ->
                    new Error(
                            emptyStringError.errorCode(),
                            emptyStringError.errorMessage(),
                            emptyStringError.fieldName().fieldName(),
                            Map.of());
            case FieldErrorWithParams fieldErrorWithParams -> {
                yield switch (fieldErrorWithParams) {
                    case GreaterThanMaxError<?> greaterThanMaxError ->
                            new Error(
                                    greaterThanMaxError.errorCode(),
                                    greaterThanMaxError.errorMessage(),
                                    greaterThanMaxError.fieldName().fieldName(),
                                    Map.of("maxValue", greaterThanMaxError.maxValue().param()));
                    case LessThanMinError<?> lessThanMinError ->
                            new Error(
                                    lessThanMinError.errorCode(),
                                    lessThanMinError.errorMessage(),
                                    lessThanMinError.fieldName().fieldName(),
                                    Map.of("minValue", lessThanMinError.minValue().param()));
                };
            }
        };
    }
}
