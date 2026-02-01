package com.sherwin.fintrac.domain.outbound;

import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.AccountId;
import com.sherwin.fintrac.domain.common.model.Email;
import java.util.Optional;

public interface AccountRepositoryPort {
    Account addAccount(Account account);

    boolean exists(Email email);

    Optional<Account> findById(AccountId accountId);
}
