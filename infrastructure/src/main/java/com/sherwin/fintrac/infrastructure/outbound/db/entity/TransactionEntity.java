package com.sherwin.fintrac.infrastructure.outbound.db.entity;

import com.sherwin.fintrac.domain.common.model.*;
import com.sherwin.fintrac.domain.transaction.Transaction;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    private @Id UUID id;
    private UUID accountId;
    private Long amount;
    private String currency;
    private String description;
    private LocalDateTime createdAt;
    private String type;

    @Version Long version;

    TransactionEntity() {}

    public TransactionEntity(
            UUID id,
            UUID accountId,
            Long amount,
            String currency,
            String description,
            LocalDateTime createdAt,
            String type) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.createdAt = createdAt;
        this.type = type;
    }

    public static TransactionEntity fromDomain(Transaction transaction) {
        return new TransactionEntity(
                transaction.id().value(),
                transaction.accountId().value(),
                transaction.amount().value(),
                transaction.amount().currencyCode().getCurrencyCode(),
                transaction.description().value(),
                transaction.createdAt().value(),
                transaction.type().getValue());
    }

    public static Transaction toDomain(TransactionEntity transactionEntity) {
        CreationResult<Transaction> transactionCreationResult =
                Transaction.of(
                        transactionEntity.getId(),
                        transactionEntity.getDescription(),
                        transactionEntity.getAmount(),
                        transactionEntity.getCurrency(),
                        transactionEntity.getCreatedAt(),
                        transactionEntity.getType(),
                        transactionEntity.getAccountId().toString());
        return transactionCreationResult.get();
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public Long getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }
}
