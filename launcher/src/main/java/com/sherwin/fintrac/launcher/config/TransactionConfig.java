package com.sherwin.fintrac.launcher.config;

import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCaseService;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfig {
    @Bean("postTransactionUseCase")
    public PostTransactionUseCase postTransactionUseCase(
            TransactionRepositoryPort transactionRepositoryPort,
            Clock clock,
            Supplier<UUID> uuidSupplier) {
        return new PostTransactionUseCaseService(transactionRepositoryPort, clock, uuidSupplier);
    }
}
