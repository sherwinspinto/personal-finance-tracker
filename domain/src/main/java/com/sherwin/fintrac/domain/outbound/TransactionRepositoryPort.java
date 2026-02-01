package com.sherwin.fintrac.domain.outbound;

import com.sherwin.fintrac.domain.common.model.AccountId;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.util.List;

public interface TransactionRepositoryPort {
    Transaction addTransaction(Transaction transaction);

    List<Transaction> getAllTransactionsForAccount(AccountId accountId);
}
