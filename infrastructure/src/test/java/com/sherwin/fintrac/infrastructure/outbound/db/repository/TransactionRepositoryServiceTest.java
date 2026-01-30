package com.sherwin.fintrac.infrastructure.outbound.db.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import com.sherwin.fintrac.infrastructure.outbound.db.entity.TransactionEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TransactionRepositoryServiceTest {
    private final TransactionRepositoryPort transactionRepositoryService;
    private final TransactionRepository transactionRepository;

    public TransactionRepositoryServiceTest() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        transactionRepositoryService = new TransactionRepositoryService(transactionRepository);
    }

    @Test
    void test_add_transaction_success() {
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 1, 10);

        CreationResult<Transaction> transactionCreationResult =
                Transaction.of(
                        transactionId,
                        "Test Transaction",
                        100L,
                        "USD",
                        createdAt,
                        "CREDIT",
                        accountId.toString());
        TransactionEntity transactionEntity =
                TransactionEntity.fromDomain(transactionCreationResult.get());
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(transactionEntity);
        Transaction transaction =
                transactionRepositoryService.addTransaction(
                        TransactionEntity.toDomain(transactionEntity));
        assertNotNull(transaction);
        assertEquals(transactionCreationResult.get(), transaction);
    }

    @Test
    void test_add_transaction_failure() {
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 1, 10);

        CreationResult<Transaction> transactionCreationResult =
                Transaction.of(
                        transactionId,
                        "Test Transaction",
                        100L,
                        "USD",
                        createdAt,
                        "CREDIT",
                        accountId.toString());

        TransactionEntity badTransactionEntity =
                new TransactionEntity(transactionId, accountId, null, null, null, null, null);

        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(badTransactionEntity);
        Exception thrownException =
                assertThrows(
                        Exception.class,
                        () ->
                                transactionRepositoryService.addTransaction(
                                        TransactionEntity.toDomain(badTransactionEntity)));
        thrownException.printStackTrace();
    }
}
