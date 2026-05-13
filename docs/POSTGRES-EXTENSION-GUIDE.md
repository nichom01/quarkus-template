# PostgreSQL Extension Guide

## Overview

The PostgreSQL extension isolates database persistence as a separate concern, following SRP. It provides:

1. Base entity class (`Entity`) that extends `PanacheEntity`
2. Base repository class (`Repository<T>`) that extends `PanacheRepository<T>`
3. Configuration for PostgreSQL connection pooling and schema management
4. Hibernate ORM + Panache infrastructure

## Architecture

```
Your Application
    ↓
Extension 5: Transaction Processor (domain: TransactionEntity, TransactionRepository)
    ↓ extends
Extension 4: PostgreSQL (infrastructure: Entity, Repository<T>, PersistenceConfig)
    ↓ uses
Quarkus Hibernate ORM
Quarkus PostgreSQL JDBC
```

## What It Provides

### 1. Entity Base Class

```java
@MappedSuperclass
public abstract class Entity extends PanacheEntity {
    // Inherited fields:
    // - id: Long (auto-generated primary key)
    
    // Inherited methods:
    // - persist()
    // - update()
    // - delete()
    // - list()
    // - find()
    // - count()
    // - etc.
}
```

### 2. Repository Base Class

```java
public abstract class Repository<T> implements PanacheRepository<T> {
    // Inherited methods:
    // - persist(T entity)
    // - persistAndFlush(T entity)
    // - delete(T entity)
    // - deleteAll()
    // - list(String query, Object... params)
    // - find(String query, Object... params)
    // - findById(Object id)
    // - count()
    // - getEntityManager()
    // - etc.
}
```

### 3. Configuration

```java
@ConfigRoot
@ConfigMapping(prefix = "quarkus.postgres")
public interface PersistenceConfig {
    String database();          // quarkus.postgres.database
    String schema();            // quarkus.postgres.schema
    boolean enableLogging();    // quarkus.postgres.enable-logging
    int maxPoolSize();          // quarkus.postgres.max-pool-size
    int minPoolSize();          // quarkus.postgres.min-pool-size
    boolean autoGenerateSchema(); // quarkus.postgres.auto-generate-schema
}
```

## How to Use

### Step 1: Add Dependency

```xml
<dependency>
    <groupId>com.neversoft.quarkus</groupId>
    <artifactId>quarkus-postgres-extension</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Step 2: Create Domain Entity

```java
@Entity
@Table(name = "transactions")
public class TransactionEntity extends Entity {
    
    @Column(nullable = false)
    public String transactionId;
    
    @Column(nullable = false)
    public BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    public TransactionStatus status;
    
    // ... other fields
}
```

### Step 3: Create Domain Repository

```java
@ApplicationScoped
public class TransactionRepository extends Repository<TransactionEntity> {
    
    public List<TransactionEntity> findByCustomerId(String customerId) {
        return list("customerId", customerId);
    }
    
    public List<TransactionEntity> findByExceptionFlag(boolean exceptionFlag) {
        return list("exceptionFlag", exceptionFlag);
    }
}
```

### Step 4: Use in Your Code

```java
@ApplicationScoped
public class TransactionService {
    
    @Inject
    TransactionRepository repository;
    
    public void saveTransaction(TransactionEntity entity) {
        repository.persist(entity);  // Inherited from Repository<T>
    }
    
    public List<TransactionEntity> listFlagged() {
        return repository.findByExceptionFlag(true);
    }
}
```

### Step 5: Configure in application.properties

```properties
# PostgreSQL Extension Configuration
quarkus.postgres.database=quarkus_db
quarkus.postgres.schema=public
quarkus.postgres.enable-logging=false
quarkus.postgres.max-pool-size=20
quarkus.postgres.min-pool-size=5

# Hibernate Configuration
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/quarkus_db
quarkus.datasource.username=quarkus_user
quarkus.datasource.password=quarkus_password
quarkus.hibernate-orm.database.generation=drop-and-create

# Logging
quarkus.log.category."org.hibernate".level=WARN
quarkus.log.category."org.hibernate.SQL".level=INFO
```

## Dependency Chain

```
Your Application Domain
    ↓ extends Entity, Repository<T> from
PostgreSQL Extension
    ↓ provides
Hibernate ORM + Panache + PostgreSQL JDBC
    ↓
Quarkus Core
```

## Reusability

This extension is reusable for any domain model with PostgreSQL:

```
✓ Order Processing
   @Entity
   public class OrderEntity extends Entity { ... }
   
   @ApplicationScoped
   public class OrderRepository extends Repository<OrderEntity> { ... }

✓ User Management
   @Entity
   public class UserEntity extends Entity { ... }
   
   @ApplicationScoped
   public class UserRepository extends Repository<UserEntity> { ... }

✓ Inventory
   @Entity
   public class InventoryEntity extends Entity { ... }
   
   @ApplicationScoped
   public class InventoryRepository extends Repository<InventoryEntity> { ... }
```

## Advanced Usage

### Custom Query Methods

```java
@ApplicationScoped
public class TransactionRepository extends Repository<TransactionEntity> {
    
    // Simple query
    public List<TransactionEntity> findByCustomerId(String customerId) {
        return list("customerId", customerId);
    }
    
    // Complex query with parameters
    public List<TransactionEntity> findHighValueWithException(BigDecimal threshold) {
        return list("amount > ?1 and exceptionFlag = true", threshold);
    }
    
    // Query with multiple parameters
    public List<TransactionEntity> findTransactionsByDateRange(LocalDate start, LocalDate end) {
        return list(
            "createdAt between ?1 and ?2",
            start.atStartOfDay(),
            end.atTime(23, 59, 59)
        );
    }
    
    // Named queries
    public long countByStatusExample(TransactionEntity.TransactionStatus status) {
        return count("status = ?1", status);
    }
}
```

### Transactions

```java
@Transactional
public void processBatchTransactions(List<TransactionEntity> entities) {
    for (TransactionEntity entity : entities) {
        repository.persist(entity);  // All persisted in single transaction
    }
    // Auto-commit at method end
}
```

### Entity Manager Access

```java
@ApplicationScoped
public class TransactionService {
    
    @Inject
    TransactionRepository repository;
    
    public void bulkUpdate() {
        // For advanced scenarios
        EntityManager em = repository.getEntityManager();
        em.createQuery("update TransactionEntity set status = ?1")
          .setParameter(1, TransactionStatus.PROCESSED)
          .executeUpdate();
    }
}
```

## Common Patterns

### Pattern 1: Simple CRUD

```java
// Create
TransactionEntity entity = new TransactionEntity();
entity.amount = BigDecimal.valueOf(100);
repository.persist(entity);

// Read
TransactionEntity found = repository.findById(1L);
List<TransactionEntity> all = repository.listAll();

// Update
found.status = TransactionStatus.APPROVED;
repository.persist(found);  // Or just update()

// Delete
repository.delete(found);
```

### Pattern 2: Domain-Specific Queries

```java
@ApplicationScoped
public class TransactionRepository extends Repository<TransactionEntity> {
    
    public Optional<TransactionEntity> findLatestByCustomer(String customerId) {
        return find(
            "customerId = ?1 order by createdAt desc",
            customerId
        ).firstResultOptional();
    }
    
    public long countExceptionsByCustomer(String customerId) {
        return count(
            "customerId = ?1 and exceptionFlag = true",
            customerId
        );
    }
}
```

### Pattern 3: Batch Operations

```java
@Transactional
public void importTransactions(List<TransactionDTO> dtos) {
    for (TransactionDTO dto : dtos) {
        TransactionEntity entity = convertToEntity(dto);
        repository.persist(entity);
    }
}

@Transactional
public void markAllAsProcessed() {
    repository.deleteAll();  // Delete all
    // Or manual update via EntityManager
}
```

## Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `quarkus.postgres.database` | quarkus_db | Database name |
| `quarkus.postgres.schema` | public | Schema name |
| `quarkus.postgres.enable-logging` | false | Enable SQL logging |
| `quarkus.postgres.max-pool-size` | 20 | Max database connections |
| `quarkus.postgres.min-pool-size` | 5 | Min database connections |
| `quarkus.postgres.auto-generate-schema` | false | Auto-create schema (set in application.properties) |

## PostgreSQL-Specific Features

### JSON Columns

```java
@Entity
public class EventEntity extends Entity {
    
    @Column(columnDefinition = "jsonb")
    public String metadata;  // Store JSON in PostgreSQL
}
```

### UUID Primary Key

```java
@Entity
public class UserEntity extends Entity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "uuid")
    public UUID id;
}
```

### Array Columns

```java
@Entity
public class TagEntity extends Entity {
    
    @Column(columnDefinition = "text[]")
    public String[] tags;
}
```

### Full Text Search

```java
@Entity
public class DocumentEntity extends Entity {
    
    @Column(columnDefinition = "tsvector")
    public String searchVector;
}
```

## Best Practices

### 1. Extend for Domain-Specific Queries

```java
// ✓ Good: Domain repository with specific queries
@ApplicationScoped
public class TransactionRepository extends Repository<TransactionEntity> {
    public List<TransactionEntity> findByExceptionFlag(boolean exceptionFlag) {
        return list("exceptionFlag", exceptionFlag);
    }
}

// ❌ Bad: Generic repository, no domain knowledge
public class GenericRepository extends Repository<TransactionEntity> {
    // Just inherits list(), find(), etc.
}
```

### 2. Use Transactions

```java
// ✓ Good: Explicit transaction boundary
@Transactional
public void processBatch(List<Data> items) {
    items.forEach(item -> repository.persist(convert(item)));
}

// ❌ Bad: Each persist is separate transaction
public void processBatch(List<Data> items) {
    items.forEach(item -> repository.persist(convert(item)));
}
```

### 3. Close Associations Early

```java
// ✓ Good: Convert to DTO to avoid lazy loading issues
public TransactionDTO getTransaction(Long id) {
    TransactionEntity entity = repository.findById(id);
    return convertToDTO(entity);  // While session is open
}

// ❌ Bad: Lazy load after session closes
public TransactionEntity getTransaction(Long id) {
    return repository.findById(id);
    // Accessing lazy collections later causes LazyInitializationException
}
```

## Troubleshooting

### Connection Pool Exhausted

```properties
# Increase pool size
quarkus.postgres.max-pool-size=50
```

### Slow Queries

```properties
# Enable SQL logging
quarkus.postgres.enable-logging=true
quarkus.log.category."org.hibernate.SQL".level=DEBUG
```

### Schema Mismatch

```properties
# Recreate schema on startup
quarkus.hibernate-orm.database.generation=drop-and-create
```

## Testing

```java
@QuarkusTest
public class TransactionRepositoryTest {
    
    @Inject
    TransactionRepository repository;
    
    @Test
    @Transactional
    public void testFindByExceptionFlag() {
        TransactionEntity entity = new TransactionEntity();
        entity.exceptionFlag = true;
        repository.persist(entity);
        
        List<TransactionEntity> flagged = repository.findByExceptionFlag(true);
        assertEquals(1, flagged.size());
    }
}
```

## Summary

The PostgreSQL extension provides:
- ✓ Generic entity/repository base classes
- ✓ Panache infrastructure
- ✓ Configuration management
- ✓ Connection pooling
- ✓ Schema generation

Domain extensions extend it to add domain-specific entities and repositories.

---

**This extension is reusable across any project needing PostgreSQL persistence with Quarkus.**
