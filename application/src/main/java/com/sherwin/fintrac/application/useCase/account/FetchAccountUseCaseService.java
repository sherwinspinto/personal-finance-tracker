package com.sherwin.fintrac.application.useCase.account;

import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.AccountId;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import java.util.Optional;
import java.util.UUID;

public class FetchAccountUseCaseService implements FetchAccountUseCase {
    private final AccountRepositoryPort accountRepositoryPort;

    public FetchAccountUseCaseService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Optional<Account> fetchAccount(String accountId) {
        UUID accountIdUUID = UUID.fromString(accountId);
        return accountRepositoryPort.findById(new AccountId(accountIdUUID));
    }

    @Override
    public Optional<Account> fetchAccountUsingPessimisticLock(String accountId) {
        UUID accountIdUUID = UUID.fromString(accountId);
        return accountRepositoryPort.fetchWithPessimisticLock(new AccountId(accountIdUUID));
    }
}
