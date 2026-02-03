package com.sherwin.fintrac.domain.outbound;

import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.AccountId;
import com.sherwin.fintrac.domain.common.model.Email;
import com.sherwin.fintrac.domain.common.model.Money;
import java.util.Optional;

public interface AccountRepositoryPort {
    Account addAccount(Account account);

    boolean exists(Email email);

    Optional<Account> findById(AccountId accountId);

    Optional<Account> fetchWithPessimisticLock(AccountId accountId);

    Long updateCurrentBalance(Money newCurrentBalance, AccountId accountId);
}
