package com.neversoft.quarkus.transaction.repository;

import com.neversoft.quarkus.postgres.runtime.repository.Repository;
import com.neversoft.quarkus.transaction.entity.TransactionEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Transaction repository extending the generic PostgreSQL Repository base class.
 *
 * Now purely domain-specific, inherits basic CRUD from Postgres extension.
 * Adds domain-specific query methods.
 */
@ApplicationScoped
public class TransactionRepository extends Repository<TransactionEntity> {

    /**
     * Find all transactions by customer ID.
     */
    public List<TransactionEntity> findByCustomerId(String customerId) {
        return list("customerId", customerId);
    }

    /**
     * Find all fraud transactions.
     */
    public List<TransactionEntity> findFraudTransactions() {
        return list("fraudDetected", true);
    }

    /**
     * Find transactions by risk level.
     */
    public List<TransactionEntity> findByRiskLevel(String riskLevel) {
        return list("riskLevel", riskLevel);
    }

    /**
     * Find transactions by status.
     */
    public List<TransactionEntity> findByStatus(TransactionEntity.TransactionStatus status) {
        return list("status", status);
    }

    /**
     * Find transaction by transaction ID.
     */
    public TransactionEntity findByTransactionId(String transactionId) {
        return find("transactionId", transactionId).firstResult();
    }

    /**
     * Count fraud transactions.
     */
    public long countFraudTransactions() {
        return count("fraudDetected", true);
    }

    /**
     * Count transactions by status.
     */
    public long countByStatus(TransactionEntity.TransactionStatus status) {
        return count("status", status);
    }
}
