package com.neversoft.quarkus.json.transform.runtime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic JSON transformation service.
 *
 * SRP: This class ONLY handles JSON→Object mapping.
 * It does NOT handle:
 * - Kafka consumption (that's Kafka Reactive extension)
 * - Business logic (that's your app)
 * - Drools rules (that's Drools extension)
 * - Persistence (that's your app)
 *
 * Reusable across all domains:
 * - REST API request bodies
 * - Kafka message parsing
 * - File processing
 * - Event transformations
 *
 * Usage:
 *
 *   @Inject
 *   JsonTransformer transformer;
 *
 *   // Deserialize JSON string to POJO
 *   MyObject obj = transformer.fromJson(jsonString, MyObject.class);
 *
 *   // Deserialize JsonNode to POJO
 *   MyObject obj = transformer.fromJsonNode(jsonNode, MyObject.class);
 *
 *   // Serialize POJO to JSON string
 *   String json = transformer.toJson(myObject);
 */
@ApplicationScoped
public class JsonTransformer {

    private static final Logger logger = LoggerFactory.getLogger(JsonTransformer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Deserialize JSON string to a target class.
     *
     * @param json The JSON string
     * @param targetClass The target POJO class
     * @param <T> The target type
     * @return The deserialized object
     */
    public <T> T fromJson(String json, Class<T> targetClass) {
        try {
            logger.debug("Deserializing JSON to {}", targetClass.getSimpleName());
            return objectMapper.readValue(json, targetClass);
        } catch (Exception e) {
            logger.error("Failed to deserialize JSON to {}", targetClass.getSimpleName(), e);
            throw new RuntimeException(
                    "JSON deserialization failed: " + e.getMessage(), e);
        }
    }

    /**
     * Convert JsonNode to target class.
     *
     * @param jsonNode The JsonNode
     * @param targetClass The target POJO class
     * @param <T> The target type
     * @return The converted object
     */
    public <T> T fromJsonNode(JsonNode jsonNode, Class<T> targetClass) {
        try {
            logger.debug("Converting JsonNode to {}", targetClass.getSimpleName());
            return objectMapper.treeToValue(jsonNode, targetClass);
        } catch (Exception e) {
            logger.error("Failed to convert JsonNode to {}", targetClass.getSimpleName(), e);
            throw new RuntimeException(
                    "JsonNode conversion failed: " + e.getMessage(), e);
        }
    }

    /**
     * Serialize object to JSON string.
     *
     * @param object The object to serialize
     * @return The JSON string
     */
    public String toJson(Object object) {
        try {
            logger.debug("Serializing {} to JSON", object.getClass().getSimpleName());
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Failed to serialize object to JSON", e);
            throw new RuntimeException(
                    "JSON serialization failed: " + e.getMessage(), e);
        }
    }

    /**
     * Parse JSON string to JsonNode.
     *
     * @param json The JSON string
     * @return The JsonNode
     */
    public JsonNode parseJson(String json) {
        try {
            logger.debug("Parsing JSON to JsonNode");
            return objectMapper.readTree(json);
        } catch (Exception e) {
            logger.error("Failed to parse JSON", e);
            throw new RuntimeException(
                    "JSON parsing failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get the underlying ObjectMapper for advanced use cases.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
