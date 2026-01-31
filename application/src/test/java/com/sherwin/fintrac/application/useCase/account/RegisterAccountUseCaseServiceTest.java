package com.sherwin.fintrac.application.useCase.account;

import static org.junit.jupiter.api.Assertions.*;

import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountCommandUseCaseOutput;
import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountUseCaseCommand;
import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.Email;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import java.time.Clock;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class RegisterAccountUseCaseServiceTest {
    static String VALID_EMAIL = "valid@fintractest.com";
    static String INVALID_EMAIL = "validfintractest.com";

    @Test
    void test_register_account_use_case_success() {
        AccountRepositoryPort accountRepositoryPort =
                createAccountRepositoryForNonExistingAccount(Boolean.FALSE);
        String accountIdString = "1db5f925-b06d-442e-94d3-3bf27c11adc8";
        Supplier<UUID> uuidSupplier = () -> UUID.fromString(accountIdString);
        Clock clock = Clock.systemUTC();
        Long amount = 100L;
        String currencyCode = "USD";
        RegisterAccountUseCaseCommand registerAccountUseCaseCommand =
                RegisterAccountUseCaseCommand.of(VALID_EMAIL, amount, currencyCode);
        RegisterAccountUseCase registerAccountUseCase =
                new RegisterAccountUseCaseService(accountRepositoryPort, uuidSupplier, clock);
        CreationResult<RegisterAccountCommandUseCaseOutput> creationResult =
                registerAccountUseCase.execute(registerAccountUseCaseCommand);
        switch (creationResult) {
            case CreationResult.Success(RegisterAccountCommandUseCaseOutput success) -> {
                assertEquals(accountIdString, success.accountId());
            }
            case CreationResult.Failure<RegisterAccountCommandUseCaseOutput>(
                            List<FieldError> validationErrors) ->
                    fail("Expected success, got failure");
        }
    }

    static AccountRepositoryPort createAccountRepositoryForNonExistingAccount(
            boolean accountExists) {
        return new AccountRepositoryPort() {
            @Override
            public Account addAccount(Account account) {
                return account;
            }

            @Override
            public boolean exists(Email email) {
                return accountExists;
            }
        };
    }
}
