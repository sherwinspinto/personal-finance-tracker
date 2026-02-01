package com.sherwin.fintrac.infrastructure.outbound.db.repository;

import com.sherwin.fintrac.domain.common.model.AccountId;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import com.sherwin.fintrac.domain.transaction.Transaction;
import com.sherwin.fintrac.infrastructure.outbound.db.entity.TransactionEntity;
import java.util.List;
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

    @Override
    public List<Transaction> getAllTransactionsForAccount(AccountId accountId) {
        return transactionRepository.findAllByAccountId(accountId.value()).stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
