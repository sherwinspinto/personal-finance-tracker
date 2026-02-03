package com.sherwin.fintrac.infrastructure.outbound.db.repository;

import com.sherwin.fintrac.infrastructure.outbound.db.entity.AccountEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    boolean existsByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(
            @QueryHint(
                    name = "jakarta.persistence.lock.timeout",
                    value = "5000")) // optional timeout
    @Query("SELECT a FROM AccountEntity a WHERE a.id = :accountId")
    Optional<AccountEntity> findByIdForUpdate(UUID accountId);

    @Modifying
    @Query("UPDATE AccountEntity a SET a.currentBalance = :currentBalance WHERE a.id = :accountId")
    Long updateCurrentBalance(UUID accountId, Long currentBalance);
}
