package com.sherwin.fintrac.application.useCase;

import static com.sherwin.fintrac.domain.common.model.CreationResult.*;

import com.sherwin.fintrac.application.useCase.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.domain.outbound.TransactionRepository;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PostTransactionUseCaseService implements PostTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final Clock clock;
    private final Supplier<UUID> uuidSupplier;

    public PostTransactionUseCaseService(
            TransactionRepository transactionRepository, Clock clock, Supplier<UUID> uuidSupplier) {
        this.transactionRepository = transactionRepository;
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
            case Success(Transaction transaction) -> success(handleCreation(transaction));
            case Failure(List<FieldError> errors) -> failure(errors);
        };
    }

    private PostTransactionUseCaseResponse handleCreation(Transaction transaction) {
        Transaction createdTransaction = transactionRepository.addTransaction(transaction);
        return PostTransactionUseCaseResponse.fromDomain(createdTransaction);
    }
}
