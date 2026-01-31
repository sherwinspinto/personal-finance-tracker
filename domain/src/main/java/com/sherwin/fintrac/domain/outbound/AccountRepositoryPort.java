package com.sherwin.fintrac.domain.outbound;

import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.Email;

public interface AccountRepositoryPort {
    Account addAccount(Account account);

    boolean exists(Email email);
}
