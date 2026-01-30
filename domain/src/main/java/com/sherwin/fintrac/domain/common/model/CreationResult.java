package com.sherwin.fintrac.domain.common.model;

import java.util.List;

public sealed interface CreationResult<T> {
    record Success<T>(T value) implements CreationResult<T> {}

    record Failure<T>(List<FieldError> validationErrors) implements CreationResult<T> {}

    default boolean failure() {
        return switch (this) {
            case Failure<T> failure -> true;
            case Success<T> success -> false;
        };
    }

    default List<FieldError> getValidationErrors() {
        return switch (this) {
            case Failure<T> failure -> failure.validationErrors();
            case Success<T> success -> List.of();
        };
    }

    default T get() {
        return switch (this) {
            case Failure<T> failure ->
                    throw new IllegalStateException("Cannot get value from failure");
            case Success<T> success -> success.value();
        };
    }

    static <T> Success<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Failure<T> failure(List<FieldError> validationErrors) {
        return new Failure<>(validationErrors);
    }
}
