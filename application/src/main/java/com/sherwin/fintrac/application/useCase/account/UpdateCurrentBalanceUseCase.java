package com.sherwin.fintrac.application.useCase.account;

public interface UpdateCurrentBalanceUseCase {
    Long updateCurrentBalance(Long newCurrentBalance, String currencyCode, String accountId);
}
