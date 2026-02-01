package com.sherwin.fintrac.application.useCase.transaction;

import static com.sherwin.fintrac.domain.common.model.CreationResult.*;

import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCase;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class PostTransactionUseCaseService implements PostTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final FetchAccountUseCase fetchAccountUseCase;
    private final Clock clock;
    private final Supplier<UUID> uuidSupplier;

    public PostTransactionUseCaseService(
            TransactionRepositoryPort transactionRepositoryPort,
            FetchAccountUseCase fetchAccountUseCase,
            Clock clock,
            Supplier<UUID> uuidSupplier) {
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.fetchAccountUseCase = fetchAccountUseCase;
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

        Optional<Account> account = fetchAccountUseCase.fetchAccount(accountIdString);

        if (account.isEmpty()) {
            return failure(
                    List.of(
                            FieldError.InvalidValue.of(
                                    FieldName.of("accountId"),
                                    ValidationParams.FieldValue.of(accountIdString))));
        }

        List<Transaction> allTransactions =
                transactionRepositoryPort.getAllTransactionsForAccount(accountId);
        List<Transaction> allTransactionsPlusCurrent = new ArrayList<>(allTransactions);
        allTransactionsPlusCurrent.add(transaction);

        Long currentBalance = account.get().calculateCurrentBalance(allTransactionsPlusCurrent);

        boolean isNegativeBalance = account.get().isBalanceNegative(currentBalance);

        return isNegativeBalance
                ? failure(
                        List.of(
                                FieldError.FieldErrorWithParams.LessThanMinError.of(
                                        FieldName.of("currentBalance"),
                                        ValidationParams.FieldValue.of(currentBalance),
                                        ValidationParams.Param.of(0L))))
                : success(handleCreation(transaction));
    }

    PostTransactionUseCaseResponse handleCreation(Transaction transaction) {
        Transaction createdTransaction = transactionRepositoryPort.addTransaction(transaction);
        return PostTransactionUseCaseResponse.fromDomain(createdTransaction);
    }
}
