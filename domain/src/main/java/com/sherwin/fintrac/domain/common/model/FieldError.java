package com.sherwin.fintrac.domain.common.model;

import static com.sherwin.fintrac.domain.common.model.ValidationParams.*;

import java.util.Objects;

public sealed interface FieldError {
    int errorCode();

    String errorMessage();

    record NullObjectError(FieldName fieldName) implements FieldError {
        public NullObjectError {
            Objects.requireNonNull(fieldName, "Field name cannot be null");
        }

        @Override
        public int errorCode() {
            return -100;
        }

        @Override
        public String errorMessage() {
            return "NULL_OBJECT";
        }

        public static NullObjectError of(FieldName fieldName) {
            return new NullObjectError(fieldName);
        }
    }

    record EmptyStringError(FieldName fieldName) implements FieldError {
        public EmptyStringError {
            Objects.requireNonNull(fieldName, "Field name cannot be null");
        }

        @Override
        public int errorCode() {
            return -104;
        }

        @Override
        public String errorMessage() {
            return "EMPTY_STRING";
        }

        public static EmptyStringError of(FieldName fieldName) {
            return new EmptyStringError(fieldName);
        }
    }

    record ConflictError(FieldName fieldName) implements FieldError {
        public ConflictError {
            Objects.requireNonNull(fieldName, "Field name cannot be null");
        }

        public static ConflictError of(FieldName fieldName) {
            return new ConflictError(fieldName);
        }

        @Override
        public int errorCode() {
            return -105;
        }

        @Override
        public String errorMessage() {
            return "CONFLICT";
        }
    }

    record InvalidValue<T>(FieldName fieldName, FieldValue<T> fieldValue) implements FieldError {
        public InvalidValue {
            Objects.requireNonNull(fieldName, "Field name cannot be null");
        }

        @Override
        public int errorCode() {
            return -101;
        }

        @Override
        public String errorMessage() {
            return "INVALID_VALUE";
        }

        public static <T> InvalidValue<T> of(FieldName fieldName, FieldValue<T> fieldValue) {
            return new InvalidValue<>(fieldName, fieldValue);
        }
    }

    sealed interface FieldErrorWithParams extends FieldError {
        record GreaterThanMaxError<T>(
                FieldName fieldName, FieldValue<T> fieldValue, Param<T> maxValue)
                implements FieldErrorWithParams {
            public GreaterThanMaxError {
                Objects.requireNonNull(fieldName, "Field name cannot be null");
            }

            @Override
            public int errorCode() {
                return -102;
            }

            @Override
            public String errorMessage() {
                return "GREATER_THAN_MAX";
            }

            public static <T> GreaterThanMaxError<T> of(
                    FieldName fieldName, FieldValue<T> fieldValue, Param<T> maxValue) {
                return new GreaterThanMaxError<>(fieldName, fieldValue, maxValue);
            }
        }

        record LessThanMinError<T>(FieldName fieldName, FieldValue<T> fieldValue, Param<T> minValue)
                implements FieldErrorWithParams {
            public LessThanMinError {
                Objects.requireNonNull(fieldName, "Field name cannot be null");
            }

            @Override
            public int errorCode() {
                return -103;
            }

            @Override
            public String errorMessage() {
                return "LESS_THAN_MIN";
            }

            public static <T> LessThanMinError<T> of(
                    FieldName fieldName, FieldValue<T> fieldValue, Param<T> minValue) {
                return new LessThanMinError<>(fieldName, fieldValue, minValue);
            }
        }
    }
}
