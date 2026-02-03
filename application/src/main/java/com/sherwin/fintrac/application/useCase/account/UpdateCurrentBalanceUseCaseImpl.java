package com.sherwin.fintrac.application.useCase.account;

import com.sherwin.fintrac.domain.common.model.AccountId;
import com.sherwin.fintrac.domain.common.model.Money;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import java.util.Currency;
import java.util.UUID;

public class UpdateCurrentBalanceUseCaseImpl implements UpdateCurrentBalanceUseCase {
    private final AccountRepositoryPort accountRepositoryPort;

    public UpdateCurrentBalanceUseCaseImpl(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Long updateCurrentBalance(
            Long newCurrentBalance, String currencyCode, String accountId) {
        return accountRepositoryPort.updateCurrentBalance(
                new Money(newCurrentBalance, Currency.getInstance(currencyCode)),
                new AccountId(UUID.fromString(accountId)));
    }
}
