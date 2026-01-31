package com.sherwin.fintrac.application.useCase.account;

import static com.sherwin.fintrac.domain.common.model.FieldError.*;

import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountCommandUseCaseOutput;
import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountUseCaseCommand;
import com.sherwin.fintrac.domain.account.Account;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.outbound.AccountRepositoryPort;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class RegisterAccountUseCaseService implements RegisterAccountUseCase {
    private final AccountRepositoryPort accountRepositoryPort;
    private final Supplier<UUID> uuidSupplier;
    private final Clock clock;

    public RegisterAccountUseCaseService(
            AccountRepositoryPort accountRepositoryPort, Supplier<UUID> uuidSupplier, Clock clock) {
        this.accountRepositoryPort = accountRepositoryPort;
        this.uuidSupplier = uuidSupplier;
        this.clock = clock;
    }

    @Override
    public CreationResult<RegisterAccountCommandUseCaseOutput> execute(
            RegisterAccountUseCaseCommand input) {
        CreationResult<Email> emailFromInput = Email.of(input.email(), FieldName.of("email"));
        return switch (emailFromInput) {
            case CreationResult.Success(Email email) -> processRegistration(input, email);
            case CreationResult.Failure(List<FieldError> validationErrors) ->
                    CreationResult.failure(validationErrors);
        };
    }

    CreationResult<RegisterAccountCommandUseCaseOutput> processRegistration(
            RegisterAccountUseCaseCommand input, Email email) {
        return accountRepositoryPort.exists(email)
                ? processWhenAccountAlreadyExists(input)
                : processWhenAccountDoesNotExist(input);
    }

    CreationResult<RegisterAccountCommandUseCaseOutput> processWhenAccountAlreadyExists(
            RegisterAccountUseCaseCommand input) {
        return CreationResult.failure(List.of(ConflictError.of(FieldName.of("email"))));
    }

    CreationResult<RegisterAccountCommandUseCaseOutput> processWhenAccountDoesNotExist(
            RegisterAccountUseCaseCommand input) {
        UUID accountId = uuidSupplier.get();
        CreationResult<Account> accountCreationResult =
                input.toDomain(accountId.toString(), LocalDateTime.now(clock));
        return switch (accountCreationResult) {
            case CreationResult.Success(Account account) -> {
                Account createdAccount = accountRepositoryPort.addAccount(account);
                yield CreationResult.success(
                        RegisterAccountCommandUseCaseOutput.fromDomain(createdAccount));
            }
            case CreationResult.Failure(List<FieldError> failure) ->
                    CreationResult.failure(failure);
        };
    }
}
