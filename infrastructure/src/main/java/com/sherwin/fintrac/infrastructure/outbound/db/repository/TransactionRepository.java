package com.sherwin.fintrac.infrastructure.outbound.db.repository;

import com.sherwin.fintrac.infrastructure.outbound.db.entity.TransactionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {}
