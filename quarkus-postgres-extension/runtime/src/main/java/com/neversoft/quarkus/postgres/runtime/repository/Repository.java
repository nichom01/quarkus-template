package com.neversoft.quarkus.postgres.runtime.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.EntityManager;

/**
 * Generic base repository for all domain entities.
 *
 * SRP: This extension provides database access patterns.
 * Domain-specific repositories extend this class.
 *
 * Your repositories can look like:
 *
 *   @ApplicationScoped
 *   public class TransactionRepository extends Repository<TransactionEntity> {
 *       public List<TransactionEntity> findByCustomerId(String customerId) {
 *           return list("customerId", customerId);
 *       }
 *
 *       public List<TransactionEntity> findByExceptionFlag(boolean exceptionFlag) {
 *           return list("exceptionFlag", exceptionFlag);
 *       }
 *   }
 *
 * Features inherited from Repository:
 * - persist(entity): void
 * - persistAndFlush(entity): void
 * - delete(entity): void
 * - deleteAll(): void
 * - list(): List
 * - find(query, params): Query
 * - count(): long
 * - getEntityManager(): EntityManager
 *
 * @param <T> The entity type
 */
public abstract class Repository<T> implements PanacheRepository<T> {
    // Inherits all PanacheRepository methods:
    // - persist(T entity)
    // - persistAndFlush(T entity)
    // - delete(T entity)
    // - deleteAll()
    // - list()
    // - list(String query, Object... params)
    // - list(String query, Map params)
    // - find(String query, Object... params)
    // - find(String query, Map params)
    // - count()
    // - count(String query, Object... params)
    // - count(String query, Map params)
    // - findAll()
    // - findById(Object id)
    // - getEntityManager()
}
