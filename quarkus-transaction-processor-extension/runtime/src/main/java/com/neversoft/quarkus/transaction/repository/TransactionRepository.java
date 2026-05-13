package com.neversoft.quarkus.transaction.repository;

import com.neversoft.quarkus.postgres.runtime.repository.Repository;
import com.neversoft.quarkus.transaction.entity.TransactionEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Transaction repository extending the generic PostgreSQL {@link Repository} base class.
 * Adds sample query helpers; naming stays free of product-specific domain terms.
 */
@ApplicationScoped
public class TransactionRepository extends Repository<TransactionEntity> {

    public List<TransactionEntity> findByCustomerId(String customerId) {
        return list("customerId", customerId);
    }

    public List<TransactionEntity> findByExceptionFlag(boolean exceptionFlag) {
        return list("exceptionFlag", exceptionFlag);
    }

    public List<TransactionEntity> findByProcessingTier(String processingTier) {
        return list("processingTier", processingTier);
    }

    public List<TransactionEntity> findByStatus(TransactionEntity.TransactionStatus status) {
        return list("status", status);
    }

    public TransactionEntity findByTransactionId(String transactionId) {
        return find("transactionId", transactionId).firstResult();
    }

    public long countByExceptionFlag(boolean exceptionFlag) {
        return count("exceptionFlag", exceptionFlag);
    }

    public long countByStatus(TransactionEntity.TransactionStatus status) {
        return count("status", status);
    }
}
