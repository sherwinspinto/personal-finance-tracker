package com.sherwin.fintrac.infrastructure.outbound.db.entity;

import com.sherwin.fintrac.domain.account.Account;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id private UUID id;

    @Column(unique = true)
    private String email;

    private String currencyCode;
    private Long initialBalance;
    private LocalDateTime createdAt;
    @Version private Long version;

    public AccountEntity() {}

    public AccountEntity(
            String id, String email, Long balance, String currencyCode, LocalDateTime createdAt) {
        this.id = UUID.fromString(id);
        this.email = email;
        this.initialBalance = balance;
        this.currencyCode = currencyCode;
        this.createdAt = createdAt;
    }

    public static AccountEntity fromDomain(Account account) {
        return new AccountEntity(
                account.id().value().toString(),
                account.email().value(),
                account.initialBalance().value(),
                account.initialBalance().currencyCode().getCurrencyCode(),
                account.createdAt().value());
    }

    public Account toDomain() {
        return Account.of(id.toString(), email, initialBalance, currencyCode, createdAt).get();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Long getBalance() {
        return initialBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
