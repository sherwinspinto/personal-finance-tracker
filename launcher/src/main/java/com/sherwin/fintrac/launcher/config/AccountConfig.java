package com.sherwin.fintrac.launcher.config;

import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCase;
import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCaseService;
import com.sherwin.fintrac.application.useCase.account.RegisterAccountUseCase;
import com.sherwin.fintrac.application.useCase.account.RegisterAccountUseCaseService;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {
    @Bean
    public RegisterAccountUseCase registerAccountUseCase(
            AccountRepositoryPort accountRepository, Supplier<UUID> uuidSupplier, Clock clock) {
        return new RegisterAccountUseCaseService(accountRepository, uuidSupplier, clock);
    }

    @Bean
    public FetchAccountUseCase fetchAccountUseCase(AccountRepositoryPort accountRepository) {
        return new FetchAccountUseCaseService(accountRepository);
    }
}
