package com.sherwin.fintrac.application.useCase.account;

import com.sherwin.fintrac.domain.account.Account;
import java.util.Optional;

public interface FetchAccountUseCase {
    Optional<Account> fetchAccount(String accountId);

    Optional<Account> fetchAccountUsingPessimisticLock(String accountId);
}
