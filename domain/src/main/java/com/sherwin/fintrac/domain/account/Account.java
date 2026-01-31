package com.sherwin.fintrac.domain.account;

import com.sherwin.fintrac.domain.common.Utils;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Account(AccountId id, Email email, Money initialBalance, CreatedAt createdAt) {
    public Account {
        Objects.requireNonNull(id, "Account id cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(createdAt, "Created at cannot be null");
        Objects.requireNonNull(initialBalance, "Initial balance cannot be null");
    }

    public static CreationResult<Account> of(
            String id,
            String email,
            Long initialBalance,
            String currencyCode,
            LocalDateTime createdAt) {
        var accountIdCreationResult = AccountId.of(id);
        var emailCreationResult = Email.of(email, FieldName.of("email"));
        var initialBalanceCreationResult =
                Money.of(
                        initialBalance,
                        currencyCode,
                        FieldName.of("initialBalance"),
                        MoneyParamsValidator);
        var createdAtCreationResult = CreatedAt.of(createdAt, FieldName.of("createdAt"));

        Optional<List<FieldError>> validationErrors =
                Utils.getListOfValidationErrorsFromListOfCreationResults(
                        List.of(
                                accountIdCreationResult,
                                emailCreationResult,
                                initialBalanceCreationResult,
                                createdAtCreationResult));

        if (validationErrors.isPresent()) {
            return CreationResult.failure(validationErrors.get());
        }

        return combine(
                        accountIdCreationResult,
                        emailCreationResult,
                        initialBalanceCreationResult,
                        createdAtCreationResult)
                .orElseThrow(() -> new IllegalStateException("Unexpected result"));
    }

    static Optional<Success<Account>> combine(
            CreationResult<AccountId> accountIdCreationResult,
            CreationResult<Email> emailCreationResult,
            CreationResult<Money> initialBalanceCreationResult,
            CreationResult<CreatedAt> createdAtCreationResult) {
        if (accountIdCreationResult instanceof Success<AccountId>(AccountId resultId)
                && emailCreationResult instanceof Success<Email>(Email resultEmail)
                && initialBalanceCreationResult instanceof Success<Money>(Money resultMoney)
                && createdAtCreationResult
                        instanceof Success<CreatedAt>(CreatedAt resultCreatedAt)) {
            return Optional.of(
                    CreationResult.success(
                            new Account(resultId, resultEmail, resultMoney, resultCreatedAt)));
        }
        return Optional.empty();
    }

    static boolean isValidInitialBalance(Long initialBalance) {
        return initialBalance != null && initialBalance >= 1;
    }

    static Money.MoneyParamsValidator MoneyParamsValidator =
            (value, currencyCode, parentFieldName) -> {
                if (isValidInitialBalance(value)) return List.of();
                return List.of(
                        FieldError.InvalidValue.of(
                                Money.VALUE.at(parentFieldName.fieldName()),
                                ValidationParams.FieldValue.of(value)));
            };
}
