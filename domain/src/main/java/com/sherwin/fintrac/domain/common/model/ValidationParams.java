package com.sherwin.fintrac.domain.common.model;

import java.util.Objects;

public interface ValidationParams {

    record FieldValue<T>(T value) implements ValidationParams {
        public static <T> FieldValue<T> of(T fieldValue) {
            return new FieldValue<>(fieldValue);
        }
    }

    record Param<T>(T param) implements ValidationParams {
        public Param {
            Objects.requireNonNull(param, "Parameter cannot be null");
        }

        public static <T> Param<T> of(T param) {
            return new Param<>(param);
        }
    }
}
