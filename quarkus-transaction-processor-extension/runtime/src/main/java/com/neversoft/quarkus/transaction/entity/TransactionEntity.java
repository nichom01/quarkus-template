package com.neversoft.quarkus.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction entity extending the generic PostgreSQL Entity base class.
 *
 * Now purely domain-specific, inherits persistence from Postgres extension.
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity extends com.neversoft.quarkus.postgres.runtime.entity.Entity {

    @Column(nullable = false, unique = true)
    public String transactionId;

    @Column(nullable = false)
    public String customerId;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TransactionType type;

    @Column(nullable = false)
    public String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TransactionStatus status;

    @Column(nullable = false)
    public LocalDateTime createdAt;

    @Column(nullable = false)
    public LocalDateTime processedAt;

    @Column(length = 500)
    public String ruleApplied;

    @Column(nullable = false)
    public String riskLevel;

    @Column(nullable = false)
    public Boolean fraudDetected;

    @Column(length = 100)
    public String merchantCategory;

    @Column(length = 2)
    public String merchantCountry;

    public TransactionEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
        this.fraudDetected = false;
        this.riskLevel = "LOW";
    }

    public enum TransactionType {
        DEBIT, CREDIT, TRANSFER, WITHDRAWAL
    }

    public enum TransactionStatus {
        PENDING, APPROVED, REJECTED, FRAUD_DETECTED
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", status=" + status +
                ", fraudDetected=" + fraudDetected +
                ", riskLevel='" + riskLevel + '\'' +
                '}';
    }
}
