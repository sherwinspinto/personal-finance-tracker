package com.sherwin.fintrac.domain.outbound;

import com.sherwin.fintrac.domain.transaction.Transaction;

public interface TransactionRepository {
    Transaction addTransaction(Transaction transaction);
}
