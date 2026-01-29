package com.sherwin.fintrac.infrastructure.outbound.db.repository;

import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import com.sherwin.fintrac.infrastructure.outbound.db.entity.TransactionEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionRepositoryService implements TransactionRepositoryPort {
    private final TransactionRepository transactionRepository;

    public TransactionRepositoryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return TransactionEntity.toDomain(
                transactionRepository.save(TransactionEntity.fromDomain(transaction)));
    }
}
