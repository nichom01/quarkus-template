package com.neversoft.quarkus.transaction.fact;

import com.neversoft.quarkus.transaction.entity.TransactionEntity;
import java.math.BigDecimal;

public class TransactionFact {
    public String transactionId;
    public String customerId;
    public BigDecimal amount;
    public TransactionEntity.TransactionType type;
    public String description;
    public TransactionEntity.TransactionStatus status;
    public String riskLevel;
    public boolean fraudDetected;
    public String ruleApplied;

    public TransactionFact() {
        this.status = TransactionEntity.TransactionStatus.PENDING;
        this.riskLevel = "LOW";
        this.fraudDetected = false;
    }

    public TransactionFact(String transactionId, String customerId, BigDecimal amount) {
        this();
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
    }
}
