package com.sherwin.fintrac.launcher.config;

import com.sherwin.fintrac.application.useCase.account.FetchAccountUseCase;
import com.sherwin.fintrac.application.useCase.account.UpdateCurrentBalanceUseCase;
import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCaseService;
import com.sherwin.fintrac.domain.outbound.TransactionRepositoryPort;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfig {
    @Bean("postTransactionUseCase")
    public PostTransactionUseCase postTransactionUseCase(
            TransactionRepositoryPort transactionRepositoryPort,
            FetchAccountUseCase fetchAccountUseCase,
            UpdateCurrentBalanceUseCase updateCurrentBalanceUseCase,
            Clock clock,
            Supplier<UUID> uuidSupplier,
            TransactionTemplate transactionTemplate) {
        PostTransactionUseCase postTransactionUseCase =
                new PostTransactionUseCaseService(
                        transactionRepositoryPort,
                        fetchAccountUseCase,
                        updateCurrentBalanceUseCase,
                        clock,
                        uuidSupplier);

        return command ->
                transactionTemplate.execute(status -> postTransactionUseCase.execute(command));
    }
}
