package com.sherwin.fintrac.domain.account;

import com.sherwin.fintrac.domain.common.Utils;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import com.sherwin.fintrac.domain.transaction.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public record Account(
        AccountId id,
        Email email,
        Money initialBalance,
        Money currentBalance,
        CreatedAt createdAt) {
    public Account {
        Objects.requireNonNull(id, "Account id cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(createdAt, "Created at cannot be null");
        Objects.requireNonNull(initialBalance, "Initial balance cannot be null");
        Objects.requireNonNull(currentBalance, "Current balance cannot be null");
    }

    public static CreationResult<Account> of(
            String id,
            String email,
            Long initialBalance,
            String currencyCode,
            Long currentBalance,
            LocalDateTime createdAt) {
        var accountIdCreationResult = AccountId.of(id);
        var emailCreationResult = Email.of(email, FieldName.of("email"));
        var initialBalanceCreationResult =
                Money.of(
                        initialBalance,
                        currencyCode,
                        FieldName.of("initialBalance"),
                        INITIAL_BALANCE_VALIDATOR);
        var currentBalanceCreationResult =
                Money.of(
                        currentBalance,
                        currencyCode,
                        FieldName.of("currentBalance"),
                        CURRENT_BALANCE_VALIDATOR);
        var createdAtCreationResult = CreatedAt.of(createdAt, FieldName.of("createdAt"));

        Optional<List<FieldError>> validationErrors =
                Utils.getListOfValidationErrorsFromListOfCreationResults(
                        List.of(
                                accountIdCreationResult,
                                emailCreationResult,
                                initialBalanceCreationResult,
                                currentBalanceCreationResult,
                                createdAtCreationResult));

        if (validationErrors.isPresent()) {
            return CreationResult.failure(validationErrors.get());
        }

        return combine(
                        accountIdCreationResult,
                        emailCreationResult,
                        initialBalanceCreationResult,
                        currentBalanceCreationResult,
                        createdAtCreationResult)
                .orElseThrow(() -> new IllegalStateException("Unexpected result"));
    }

    static Optional<Success<Account>> combine(
            CreationResult<AccountId> accountIdCreationResult,
            CreationResult<Email> emailCreationResult,
            CreationResult<Money> initialBalanceCreationResult,
            CreationResult<Money> currentBalanceCreationResult,
            CreationResult<CreatedAt> createdAtCreationResult) {
        if (accountIdCreationResult instanceof Success<AccountId>(AccountId resultId)
                && emailCreationResult instanceof Success<Email>(Email resultEmail)
                && initialBalanceCreationResult instanceof Success<Money>(Money resultMoney)
                && currentBalanceCreationResult
                        instanceof Success<Money>(Money resultCurrentBalance)
                && createdAtCreationResult
                        instanceof Success<CreatedAt>(CreatedAt resultCreatedAt)) {
            return Optional.of(
                    CreationResult.success(
                            new Account(
                                    resultId,
                                    resultEmail,
                                    resultMoney,
                                    resultCurrentBalance,
                                    resultCreatedAt)));
        }
        return Optional.empty();
    }

    static boolean isValidInitialBalance(Long initialBalance) {
        return initialBalance != null && initialBalance >= 1;
    }

    static Money.MoneyParamsValidator INITIAL_BALANCE_VALIDATOR =
            (value, currencyCode, parentFieldName) -> {
                if (isValidInitialBalance(value)) return List.of();
                return List.of(
                        FieldError.InvalidValue.of(
                                Money.VALUE.at(parentFieldName.fieldName()),
                                ValidationParams.FieldValue.of(value)));
            };

    static Money.MoneyParamsValidator CURRENT_BALANCE_VALIDATOR =
            (value, currencyCode, parentFieldName) -> {
                if (value >= 0) return List.of();
                return List.of(
                        FieldError.InvalidValue.of(
                                Money.VALUE.at(parentFieldName.fieldName()),
                                ValidationParams.FieldValue.of(value)));
            };

    public CreationResult<Void> canApply(Transaction transaction) {
        Long newCurrentBalance = computeNewBalanceAfterReceivingLatestTransaction(transaction);
        if (newCurrentBalance < 0) {
            return CreationResult.failure(
              List.of(
                FieldError.FieldErrorWithParams.LessThanMinError.of(
                  FieldName.of("currentBalance"),
                  ValidationParams.FieldValue.of(newCurrentBalance),
                  ValidationParams.Param.of(0L))));
        }
        return CreationResult.success(null);
    }

    public Account apply(Transaction transaction) {
        return withBalance(
                computeNewBalanceAfterReceivingLatestTransaction(transaction));
    }

    public Long computeNewBalanceAfterReceivingLatestTransaction(Transaction transaction) {
        Function<Long, Long> signFunction = transaction.type().getSignFunction();
        Long transactionAmountWithSign = signFunction.apply(transaction.amount().value());
        return currentBalance.value() + transactionAmountWithSign;
    }

    Account withBalance(Long newCurrentBalance) {
        return new Account(
                new AccountId(id.value()),
                new Email(email().value()),
                new Money(initialBalance().value(), initialBalance.currencyCode()),
                new Money(newCurrentBalance, initialBalance.currencyCode()),
                new CreatedAt(createdAt.value()));
    }
}
