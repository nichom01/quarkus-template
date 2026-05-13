package com.neversoft.quarkus.kafka.reactive.runtime;

/**
 * Functional interface for message processing.
 * Allows applications to plug in their own message handling logic.
 *
 * Usage in your app:
 *   @Inject
 *   MessageProcessor processor;
 *
 *   processor.process(message);
 */
@FunctionalInterface
public interface MessageProcessor {

    /**
     * Process a single Kafka message.
     *
     * @param message The raw message string from Kafka
     * @throws Exception if processing fails
     */
    void process(String message) throws Exception;
}
