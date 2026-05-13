package com.neversoft.quarkus.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Sample transaction aggregate for the transaction-processor extension.
 * Field names are intentionally generic so the extension stays reusable.
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
    public String policyOutcome;

    @Column(nullable = false)
    public String processingTier;

    @Column(nullable = false)
    public Boolean exceptionFlag;

    @Column(length = 100)
    public String merchantCategory;

    @Column(length = 2)
    public String merchantCountry;

    public TransactionEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
        this.exceptionFlag = false;
        this.processingTier = "LOW";
    }

    public enum TransactionType {
        DEBIT, CREDIT, TRANSFER, WITHDRAWAL
    }

    public enum TransactionStatus {
        PENDING, APPROVED, REJECTED, ESCALATED
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
                ", exceptionFlag=" + exceptionFlag +
                ", processingTier='" + processingTier + '\'' +
                '}';
    }
}
