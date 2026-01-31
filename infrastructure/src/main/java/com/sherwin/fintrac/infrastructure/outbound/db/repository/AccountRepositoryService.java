package com.sherwin.fintrac.infrastructure.outbound.db.repository;

import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.Email;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import com.sherwin.fintrac.infrastructure.outbound.db.entity.AccountEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountRepositoryService implements AccountRepositoryPort {
    private final AccountRepository accountRepository;

    public AccountRepositoryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account addAccount(Account account) {
        return accountRepository.save(AccountEntity.fromDomain(account)).toDomain();
    }

    @Override
    public boolean exists(Email email) {
        return accountRepository.existsByEmail(email.value());
    }
}
