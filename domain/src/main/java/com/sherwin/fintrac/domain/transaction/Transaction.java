package com.sherwin.fintrac.domain.transaction;

import com.sherwin.fintrac.domain.common.Validations;
import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.common.model.CreationResult.Failure;
import com.sherwin.fintrac.domain.common.model.CreationResult.Success;
import com.sherwin.fintrac.domain.common.model.FieldError.InvalidValue;
import com.sherwin.fintrac.domain.common.model.ValidationParams.FieldValue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record Transaction(
        TransactionId id,
        Text description,
        Money amount,
        CreatedAt createdAt,
        Type type,
        AccountId accountId) {
    private static final int DESCRIPTION_DEFAULT_MAX_LENGTH = 255;

    public enum Type {
        CREDIT("CREDIT"),
        DEBIT("DEBIT");
        private final String value;
        private static final FieldName fieldName = FieldName.of("type");

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
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
            UUID accountId) {
        var idCreationResult = TransactionId.of(transactionId);
        var descriptionCreationResult =
                Text.of(description, FieldName.of("description"), DESCRIPTION_DEFAULT_MAX_LENGTH);
        var amountCreationResult = Money.of(amount, currencyCode, FieldName.of("amount"));
        var createdAtCreationResult = CreatedAt.of(createdAt, FieldName.of("createdAt"));
        var typeCreationResult = Type.of(type);
        var accountIdCreationResult = AccountId.of(accountId);

        List<FieldError> validationErrors =
                Stream.of(
                                idCreationResult,
                                descriptionCreationResult,
                                amountCreationResult,
                                createdAtCreationResult,
                                typeCreationResult,
                                accountIdCreationResult)
                        .filter(CreationResult::failure)
                        .map(CreationResult::getValidationErrors)
                        .flatMap(List::stream)
                        .toList();

        if (!validationErrors.isEmpty()) {
            return CreationResult.failure(validationErrors);
        }

        if (idCreationResult instanceof Success<TransactionId>(TransactionId resultId)
                && descriptionCreationResult instanceof Success<Text>(Text resultDescription)
                && amountCreationResult instanceof Success<Money>(Money resultAmount)
                && createdAtCreationResult instanceof Success<CreatedAt>(CreatedAt resultCreatedAt)
                && typeCreationResult instanceof Success<Type>(Type resultType)
                && accountIdCreationResult
                        instanceof Success<AccountId>(AccountId resultAccountId)) {
            return CreationResult.success(
                    new Transaction(
                            resultId,
                            resultDescription,
                            resultAmount,
                            resultCreatedAt,
                            resultType,
                            resultAccountId));
        }
        throw new IllegalStateException("Unexpected result");
    }
}
