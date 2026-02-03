package com.sherwin.fintrac.application.useCase.transaction;

import static com.sherwin.fintrac.domain.common.model.CreationResult.*;

import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCase;
import com.sherwin.fintrac.application.useCase.account.UpdateCurrentBalanceUseCase;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class PostTransactionUseCaseService implements PostTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final FetchAccountUseCase fetchAccountUseCase;
    private final UpdateCurrentBalanceUseCase updateCurrentBalanceUseCase;
    private final Clock clock;
    private final Supplier<UUID> uuidSupplier;

    public PostTransactionUseCaseService(
            TransactionRepositoryPort transactionRepositoryPort,
            FetchAccountUseCase fetchAccountUseCase,
            UpdateCurrentBalanceUseCase updateCurrentBalanceUseCase,
            Clock clock,
            Supplier<UUID> uuidSupplier) {
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.fetchAccountUseCase = fetchAccountUseCase;
        this.updateCurrentBalanceUseCase = updateCurrentBalanceUseCase;
        this.clock = clock;
        this.uuidSupplier = uuidSupplier;
    }

    @Override
    public CreationResult<PostTransactionUseCaseResponse> execute(
            PostTransactionCommand postTransactionCommand) {
        // TODO: We need to validate accountId exists?
        // TODO: Do we need Input validation? For now leaving validation out will come back after
        // initial implementation
        UUID transactionId = uuidSupplier.get();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        CreationResult<Transaction> transactionCreationResult =
                Transaction.of(
                        transactionId,
                        postTransactionCommand.description(),
                        postTransactionCommand.amount(),
                        postTransactionCommand.currencyCode(),
                        createdAt,
                        postTransactionCommand.type(),
                        postTransactionCommand.accountId());

        return switch (transactionCreationResult) {
            case Success(Transaction transaction) -> processTransaction(transaction);
            case Failure(List<FieldError> errors) -> failure(errors);
        };
    }

    CreationResult<PostTransactionUseCaseResponse> processTransaction(Transaction transaction) {
        String accountIdString = transaction.accountId().value().toString();
        AccountId accountId = transaction.accountId();

        Optional<Account> account =
                fetchAccountUseCase.fetchAccountUsingPessimisticLock(accountIdString);

        if (account.isEmpty()) {
            return failure(
                    List.of(
                            FieldError.InvalidValue.of(
                                    FieldName.of("accountId"),
                                    ValidationParams.FieldValue.of(accountIdString))));
        }

        CreationResult<Void> canApplyTransaction = account.get().canApply(transaction);

        return switch (canApplyTransaction) {
            case Success<Void> _ -> {
                Account updatedAccount = account.get().apply(transaction);

                Transaction createdTransaction =
                        transactionRepositoryPort.addTransaction(transaction);
                Long dbUpdatedCurrentBalance =
                        updateCurrentBalanceUseCase.updateCurrentBalance(
                                updatedAccount.currentBalance().value(),
                                account.get().currentBalance().currencyCode().getCurrencyCode(),
                                accountId.value().toString());
                yield new Success<>(PostTransactionUseCaseResponse.fromDomain(createdTransaction));
            }
            case Failure<Void> (var errors) -> failure(errors);
        };
    }
}
