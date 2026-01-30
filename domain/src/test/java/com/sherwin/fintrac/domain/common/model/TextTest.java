package com.sherwin.fintrac.domain.common.model;

import static org.junit.jupiter.api.Assertions.*;

import com.sherwin.fintrac.domain.common.model.FieldError.FieldErrorWithParams.GreaterThanMaxError;
import org.junit.jupiter.api.Test;

class TextTest {
    @Test
    void test_text_creation() {
        int maxLength = 255;
        CreationResult<Text> creationResult =
                Text.of("Test description", FieldName.of("description"), maxLength);
        switch (creationResult) {
            case CreationResult.Success<Text> success ->
                    assertEquals("Test description", success.value().value());
            case CreationResult.Failure<Text> failure -> fail("Expected success, got failure");
        }
    }

    @Test
    void test_text_creation_failure_length_greater_than_max() {
        int maxLength = 10;
        String descriptionText = "Test description with more than 255 characters";
        CreationResult<Text> creationResult =
                Text.of(descriptionText, FieldName.of("description"), maxLength);
        switch (creationResult) {
            case CreationResult.Success<Text> success -> fail("Expected failure, got success");
            case CreationResult.Failure<Text> failure -> {
                assertEquals(1, failure.validationErrors().size());
                assertEquals(-102, failure.validationErrors().getFirst().errorCode());
                assertEquals(
                        "GREATER_THAN_MAX", failure.validationErrors().getFirst().errorMessage());

                FieldError firstError = failure.validationErrors().getFirst();
                GreaterThanMaxError<Integer> greaterThanMaxError =
                        assertInstanceOf(GreaterThanMaxError.class, firstError);

                assertEquals(FieldName.of("description"), greaterThanMaxError.fieldName());
                assertEquals(maxLength, greaterThanMaxError.maxValue().param());
            }
        }
    }

    @Test
    void test_text_creation_failure_null_value() {
        CreationResult<Text> creationResult = Text.of(null, FieldName.of("description"), 10);
        switch (creationResult) {
            case CreationResult.Success<Text> success -> fail("Expected failure, got success");
            case CreationResult.Failure<Text> failure -> {
                assertEquals(1, failure.validationErrors().size());
                assertEquals(-104, failure.validationErrors().getFirst().errorCode());
                assertEquals("EMPTY_STRING", failure.validationErrors().getFirst().errorMessage());
            }
        }
    }

    @Test
    void test_text_creation_failure_empty_value() {
        CreationResult<Text> creationResult = Text.of("", FieldName.of("description"), 10);
        switch (creationResult) {
            case CreationResult.Success<Text> success -> fail("Expected failure, got success");
            case CreationResult.Failure<Text> failure -> {
                assertEquals(1, failure.validationErrors().size());
                assertEquals(-104, failure.validationErrors().getFirst().errorCode());
                assertEquals("EMPTY_STRING", failure.validationErrors().getFirst().errorMessage());
            }
        }
    }
}
