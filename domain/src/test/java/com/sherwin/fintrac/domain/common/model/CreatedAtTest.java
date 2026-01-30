package com.sherwin.fintrac.domain.common.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class CreatedAtTest {
    private static final FieldName FIELD_NAME = FieldName.of("createdAt");

    @Test
    void test_created_at_creation() {
        LocalDateTime currentDateTime = LocalDateTime.of(2026, 1, 1, 1, 1);
        CreationResult<CreatedAt> createdAt = CreatedAt.of(currentDateTime, FIELD_NAME);
        switch (createdAt) {
            case CreationResult.Success(CreatedAt ca) -> assertEquals(currentDateTime, ca.value());
            case CreationResult.Failure<CreatedAt> _ ->
                    fail("CreatedAt creation should not fail with valid LocalDateTime");
        }
    }

    @Test
    void test_created_at_null_creation() {
        CreationResult<CreatedAt> createdAt = CreatedAt.of(null, FIELD_NAME);
        switch (createdAt) {
            case CreationResult.Success(CreatedAt ca) ->
                    fail("CreatedAt creation should fail with null LocalDateTime");
            case CreationResult.Failure<CreatedAt> fe -> {
                assertEquals(1, fe.validationErrors().size());
                assertEquals(-100, fe.validationErrors().getFirst().errorCode());
                assertEquals("NULL_OBJECT", fe.validationErrors().getFirst().errorMessage());
            }
        }
    }
}
