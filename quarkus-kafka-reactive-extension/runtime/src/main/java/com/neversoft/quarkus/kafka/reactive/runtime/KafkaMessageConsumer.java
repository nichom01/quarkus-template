package com.neversoft.quarkus.kafka.reactive.runtime;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base for Kafka consumers.
 *
 * SRP: This class only handles Kafka message consumption.
 * It does NOT handle:
 * - JSON parsing (use JsonTransformer)
 * - Business logic (subclass handles it)
 * - Persistence (subclass handles it)
 * - Rules execution (subclass handles it)
 *
 * Usage in your app:
 *
 *   @ApplicationScoped
 *   public class MyConsumer extends KafkaMessageConsumer {
 *
 *       @Inject
 *       MyBusinessLogic logic;
 *
 *       @Incoming("my-topic")
 *       public void consumeMessage(String message) {
 *           super.processMessage(message, logic::handle);
 *       }
 *   }
 */
public abstract class KafkaMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    /**
     * Process a message with error handling.
     * Subclasses call this from their @Incoming method.
     *
     * @param message The raw message from Kafka
     * @param processor The processor to handle the message
     */
    protected void processMessage(String message, MessageProcessor processor) {
        try {
            logger.debug("Processing Kafka message");
            processor.process(message);
            logger.debug("Message processed successfully");
        } catch (Exception e) {
            logger.error("Error processing message", e);
            onProcessingError(message, e);
        }
    }

    /**
     * Handle processing errors.
     * Override in subclass to send to DLQ, alert, etc.
     *
     * @param message The message that failed
     * @param exception The exception that occurred
     */
    protected void onProcessingError(String message, Exception exception) {
        logger.error("Message processing failed: {}", exception.getMessage());
        // Subclass can override for DLQ, alerting, etc.
    }
}
