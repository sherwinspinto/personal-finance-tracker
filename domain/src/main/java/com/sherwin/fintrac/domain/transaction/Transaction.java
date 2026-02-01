package com.sherwin.fintrac.domain.transaction;

import com.sherwin.fintrac.domain.common.Utils;
import com.sherwin.fintrac.domain.common.Validations;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.common.model.CreationResult.Failure;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import com.sherwin.fintrac.domain.common.model.FieldError.InvalidValue;
import com.sherwin.fintrac.domain.common.model.ValidationParams.FieldValue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public record Transaction(
        TransactionId id,
        Text description,
        Money amount,
        CreatedAt createdAt,
        Type type,
        AccountId accountId) {
    private static final int DESCRIPTION_DEFAULT_MAX_LENGTH = 255;

    public enum Type {
        CREDIT("CREDIT", amount -> amount),
        DEBIT("DEBIT", amount -> -amount);

        private final String value;
        private final Function<Long, Long> signFunction;
        private static final FieldName fieldName = FieldName.of("type");

        Type(String value, Function<Long, Long> signFunction) {
            this.value = value;
            this.signFunction = signFunction;
        }

        public String getValue() {
            return value;
        }

        public Function<Long, Long> getSignFunction() {
            return signFunction;
        }

        public static CreationResult<Type> of(String value) {
            if (Validations.isNullOrEmpty(value)) {
                return new Failure<>(List.of(InvalidValue.of(fieldName, FieldValue.of(value))));
            }
            try {
                Type type = Type.valueOf(value);
                return CreationResult.success(type);
            } catch (IllegalArgumentException e) {
                return new Failure<>(List.of(InvalidValue.of(fieldName, FieldValue.of(value))));
            }
        }
    }

    public static CreationResult<Transaction> of(
            UUID transactionId,
            String description,
            Long amount,
            String currencyCode,
            LocalDateTime createdAt,
            String type,
            String accountId) {
        var idCreationResult = TransactionId.of(transactionId);
        var descriptionCreationResult =
                Text.of(description, FieldName.of("description"), DESCRIPTION_DEFAULT_MAX_LENGTH);
        var amountCreationResult = Money.of(amount, currencyCode, FieldName.of("amount"));
        var createdAtCreationResult = CreatedAt.of(createdAt, FieldName.of("createdAt"));
        var typeCreationResult = Type.of(type);
        var accountIdCreationResult = AccountId.of(accountId);

        Optional<List<FieldError>> validationErrors =
                Utils.getListOfValidationErrorsFromListOfCreationResults(
                        List.of(
                                idCreationResult,
                                descriptionCreationResult,
                                amountCreationResult,
                                createdAtCreationResult,
                                typeCreationResult,
                                accountIdCreationResult));

        if (validationErrors.isPresent()) {
            return CreationResult.failure(validationErrors.get());
        }

        return combine(
                        idCreationResult,
                        descriptionCreationResult,
                        amountCreationResult,
                        createdAtCreationResult,
                        typeCreationResult,
                        accountIdCreationResult)
                .orElseThrow(() -> new IllegalStateException("Unexpected result"));
    }

    static Optional<Success<Transaction>> combine(
            CreationResult<TransactionId> idCreationResult,
            CreationResult<Text> descriptionCreationResult,
            CreationResult<Money> amountCreationResult,
            CreationResult<CreatedAt> createdAtCreationResult,
            CreationResult<Type> typeCreationResult,
            CreationResult<AccountId> accountIdCreationResult) {
        if (idCreationResult instanceof Success<TransactionId>(TransactionId resultId)
                && descriptionCreationResult instanceof Success<Text>(Text resultDescription)
                && amountCreationResult instanceof Success<Money>(Money resultAmount)
                && createdAtCreationResult instanceof Success<CreatedAt>(CreatedAt resultCreatedAt)
                && typeCreationResult instanceof Success<Type>(Type resultType)
                && accountIdCreationResult
                        instanceof Success<AccountId>(AccountId resultAccountId)) {
            return Optional.of(
                    CreationResult.success(
                            new Transaction(
                                    resultId,
                                    resultDescription,
                                    resultAmount,
                                    resultCreatedAt,
                                    resultType,
                                    resultAccountId)));
        }
        return Optional.empty();
    }
}
