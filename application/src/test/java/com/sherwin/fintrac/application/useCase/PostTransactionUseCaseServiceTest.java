package com.sherwin.fintrac.application.useCase;

import static org.junit.jupiter.api.Assertions.*;

import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCaseService;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.domain.common.model.FieldName;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.Clock;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PostTransactionUseCaseServiceTest {
    @Test
    void test_post_transaction_use_case_success() {
        UUID transactionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        PostTransactionUseCase useCase = createUseCase(transactionId);
        assertNotNull(useCase);
        PostTransactionCommand command =
                new PostTransactionCommand(
                        accountId.toString(), 100L, "USD", "CREDIT", "Test description");
        CreationResult<PostTransactionUseCaseResponse> result = useCase.execute(command);
        assertNotNull(result);
        switch (result) {
            case CreationResult.Success(PostTransactionUseCaseResponse success) -> {
                assertNotNull(success);
                assertEquals(command.accountId(), success.accountId());
                assertEquals(command.amount(), success.amount());
                assertEquals(command.type(), success.type());
                assertEquals(command.description(), success.description());
                assertEquals(transactionId.toString(), success.transactionId());
            }
            case CreationResult.Failure<PostTransactionUseCaseResponse> failure ->
                    fail("Expected success, but got failure: ");
        }
    }

    @Test
    void test_post_transaction_use_case_failure() {
        UUID transactionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        PostTransactionUseCase useCase = createUseCase(transactionId);
        assertNotNull(useCase);
        PostTransactionCommand command =
                new PostTransactionCommand(
                        accountId.toString(), 100L, "US", "CREDIT", "Test description");
        CreationResult<PostTransactionUseCaseResponse> result = useCase.execute(command);
        assertNotNull(result);
        switch (result) {
            case CreationResult.Success(PostTransactionUseCaseResponse success) ->
                    fail("Expected failure, but got success: ");
            case CreationResult.Failure<PostTransactionUseCaseResponse> failure -> {
                assertEquals(1, failure.validationErrors().size());
                assertEquals(-101, failure.validationErrors().getFirst().errorCode());
                assertEquals("INVALID_VALUE", failure.validationErrors().getFirst().errorMessage());
                assertEquals(
                        FieldError.InvalidValue.class,
                        failure.validationErrors().getFirst().getClass());
                FieldError.InvalidValue<String> invalidValueError =
                        (FieldError.InvalidValue<String>) failure.validationErrors().getFirst();
                assertEquals(FieldName.of("amount.currencyCode"), invalidValueError.fieldName());
                assertEquals("US", invalidValueError.fieldValue().value());
            }
        }
    }

    static PostTransactionUseCase createUseCase(UUID transactionId) {
        return new PostTransactionUseCaseService(
                createTransactionRepository(), Clock.systemUTC(), () -> transactionId);
    }

    static TransactionRepositoryPort createTransactionRepository() {
        return new TransactionRepositoryPort() {
            @Override
            public Transaction addTransaction(Transaction transaction) {
                return transaction;
            }
        };
    }
}
