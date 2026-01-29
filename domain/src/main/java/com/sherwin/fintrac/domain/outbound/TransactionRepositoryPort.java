package com.sherwin.fintrac.domain.outbound;

import com.sherwin.fintrac.domain.transaction.Transaction;

public interface TransactionRepositoryPort {
    Transaction addTransaction(Transaction transaction);
}
