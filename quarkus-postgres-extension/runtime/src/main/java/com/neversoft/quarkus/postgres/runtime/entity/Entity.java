package com.neversoft.quarkus.postgres.runtime.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.MappedSuperclass;

/**
 * Base entity class for all domain entities.
 *
 * SRP: This extension provides persistence infrastructure.
 * Domain-specific entities extend this class.
 *
 * Your entities can look like:
 *
 *   @Entity
 *   @Table(name = "transactions")
 *   public class TransactionEntity extends Entity {
 *       @Column
 *       public String transactionId;
 *
 *       @Column
 *       public BigDecimal amount;
 *       // ... other fields
 *   }
 *
 * Features inherited from Entity:
 * - id field (auto-generated primary key)
 * - Panache query methods (list, find, count, delete, etc.)
 * - persist() method
 * - update() method
 * - delete() method
 */
@MappedSuperclass
public abstract class Entity extends PanacheEntity {
    // Inherits:
    // - id: Long (auto-generated primary key)
    // - persist(): void
    // - update(): void
    // - delete(): void
    // - list(): List
    // - find(): Query
    // - count(): long
    // - listAll(): List
    // - deleteAll(): void
}
