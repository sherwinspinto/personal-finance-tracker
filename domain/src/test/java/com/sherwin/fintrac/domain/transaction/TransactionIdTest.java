package com.sherwin.fintrac.domain.transaction;

import static com.sherwin.fintrac.domain.common.model.CreationResult.Failure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TransactionIdTest {
    @Test
    void test_creation_success() {
        UUID input = UUID.randomUUID();
        final var transactionIdCreationResult = TransactionId.of(input);
        switch (transactionIdCreationResult) {
            case Success<TransactionId> success -> assertEquals(input, success.value().value());
            case Failure<TransactionId> failure -> fail("Expected failure, got success");
        }
    }

    @Test
    void test_creation_failure() {
        final var transactionIdCreationResult = TransactionId.of(null);
        switch (transactionIdCreationResult) {
            case Success<TransactionId> success -> fail("Expected failure, got success");
            case Failure<TransactionId> failure -> {
                assertEquals(1, failure.validationErrors().size());
                assertEquals(-100, failure.validationErrors().getFirst().errorCode());
            }
        }
    }
}
