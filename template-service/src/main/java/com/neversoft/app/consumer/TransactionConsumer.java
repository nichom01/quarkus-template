package com.neversoft.app.consumer;

import com.neversoft.quarkus.drools.runtime.DroolsEngineService;
import com.neversoft.quarkus.json.transform.runtime.JsonTransformer;
import com.neversoft.quarkus.kafka.reactive.runtime.KafkaMessageConsumer;
import com.neversoft.quarkus.transaction.entity.TransactionEntity;
import com.neversoft.quarkus.transaction.fact.TransactionFact;
import com.neversoft.quarkus.transaction.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TransactionConsumer extends KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

    @Inject
    JsonTransformer transformer;

    @Inject
    DroolsEngineService engine;

    @Inject
    TransactionRepository repo;

    @Incoming("transactions")
    @Transactional
    public void consume(String message) {
        processMessage(message, this::handleTransaction);
    }

    private void handleTransaction(String message) throws Exception {
        // 1. Parse JSON to TransactionFact
        TransactionFact fact = transformer.fromJson(message, TransactionFact.class);

        // 2. Execute Drools rules
        TransactionFact result = engine.execute(fact);

        // 3. Create entity from fact
        TransactionEntity entity = new TransactionEntity();
        entity.transactionId = result.transactionId;
        entity.customerId = result.customerId;
        entity.amount = result.amount;
        entity.type = result.type;
        entity.description = result.description;
        entity.status = result.status;
        entity.riskLevel = result.riskLevel;
        entity.fraudDetected = result.fraudDetected;
        entity.ruleApplied = result.ruleApplied;

        // 4. Persist to database
        repo.persist(entity);
    }

    @Override
    protected void onProcessingError(String message, Exception e) {
        // TODO: Send to dead letter queue
        // TODO: Alert fraud detection team
        log.error("Error processing transaction: " + message, e);
    }
}
