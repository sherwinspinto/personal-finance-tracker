package com.sherwin.fintrac.application.useCase;

import static org.junit.jupiter.api.Assertions.*;

import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCase;
import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCaseService;
import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCaseService;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PostTransactionUseCaseServiceTest {
    @Test
    void test_post_transaction_use_case_success() {
        UUID transactionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        Account account =
                new Account(
                        new AccountId(accountId),
                        new Email("test@email.com"),
                        new Money(1000L, Currency.getInstance("USD")),
                        new CreatedAt(LocalDateTime.now()));
        PostTransactionUseCase useCase = createUseCase(transactionId, account, true, account);
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
        Account account =
                new Account(
                        new AccountId(accountId),
                        new Email("test@email.com"),
                        new Money(1000L, Currency.getInstance("USD")),
                        new CreatedAt(LocalDateTime.now()));
        PostTransactionUseCase useCase = createUseCase(transactionId, account, false, null);
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

    static PostTransactionUseCase createUseCase(
            UUID transactionId, Account addCount, boolean exists, Account findById) {
        return new PostTransactionUseCaseService(
                createTransactionRepository(),
                createFetchAccountUseCase(addCount, exists, findById),
                Clock.systemUTC(),
                () -> transactionId);
    }

    static FetchAccountUseCase createFetchAccountUseCase(
            Account addCount, boolean exists, Account findById) {
        return new FetchAccountUseCaseService(createAccountRepository(addCount, exists, findById));
    }

    private static AccountRepositoryPort createAccountRepository(
            Account addCount, boolean exists, Account findById) {
        return new AccountRepositoryPort() {
            @Override
            public Account addAccount(Account account) {
                return null;
            }

            @Override
            public boolean exists(Email email) {
                return false;
            }

            @Override
            public Optional<Account> findById(AccountId accountId) {
                return Optional.of(findById);
            }
        };
    }

    static TransactionRepositoryPort createTransactionRepository() {
        return new TransactionRepositoryPort() {
            @Override
            public Transaction addTransaction(Transaction transaction) {
                return transaction;
            }

            @Override
            public List<Transaction> getAllTransactionsForAccount(AccountId accountId) {
                return List.of();
            }
        };
    }
}
