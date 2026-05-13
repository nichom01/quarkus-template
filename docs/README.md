# Quarkus Extensions - Five Focused Extensions

A production-ready ecosystem of five independently reusable Quarkus extensions following KISS and Single Responsibility Principle.

## 📦 The Extensions

### 1. Drools Extension
**Responsibility**: Execute Drools rules on fact objects
- `quarkus-drools-extension/pom.xml` (parent)
- `quarkus-drools-extension/runtime/pom.xml` (runtime)
- `quarkus-drools-extension/deployment/pom.xml` (deployment)

### 2. Kafka Reactive Extension
**Responsibility**: Consume Kafka messages
- `quarkus-kafka-reactive-extension/pom.xml` (parent)
- `quarkus-kafka-reactive-extension/runtime/pom.xml` (runtime)
  - `MessageProcessor.java` (processor interface)
  - `KafkaMessageConsumer.java` (consumer base)
- `quarkus-kafka-reactive-extension/deployment/pom.xml` (deployment)

### 3. JSON Transform Extension
**Responsibility**: Transform JSON ↔ Objects
- `quarkus-json-transform-extension/pom.xml` (parent)
- `quarkus-json-transform-extension/runtime/pom.xml` (runtime)
  - `JsonTransformer.java` (transformation service)
- `quarkus-json-transform-extension/deployment/pom.xml` (deployment)

### 4. PostgreSQL Extension ⭐
**Responsibility**: Provide database persistence infrastructure
- `quarkus-postgres-extension/pom.xml` (parent)
- `quarkus-postgres-extension/runtime/pom.xml` (runtime)
  - `PersistenceConfig.java` (configuration)
  - `Entity.java` (base entity class)
  - `Repository.java` (generic repository)
- `quarkus-postgres-extension/deployment/pom.xml` (deployment)

### 5. Transaction Processor Extension
**Responsibility**: Transaction domain model
- `quarkus-transaction-processor-extension/pom.xml` (parent)
- `quarkus-transaction-processor-extension/runtime/pom.xml` (runtime)
  - `TransactionEntity.java` (JPA entity)
  - `TransactionRepository.java` (domain repository)
- `quarkus-transaction-processor-extension/deployment/pom.xml` (deployment)

## 🎯 Reference Application

**fraud-detection-service** - Demonstrates composition
- `fraud-detection-service/pom.xml` - Depends on all 5 extensions
- `fraud-detection-service/src/main/java/com/neversoft/app/consumer/TransactionConsumer.java` - Application code (~50 lines)
- `fraud-detection-service/src/main/resources/rules/transaction-fraud-rules.drl` - Drools rules

## 📚 Documentation

- **FINAL-ARCHITECTURE.md** - Complete system architecture with all 5 extensions
- **POSTGRES-EXTENSION-GUIDE.md** - Deep dive into the PostgreSQL extension
- **COMPLETE-FILES-INDEX.md** - Detailed file inventory

## 🏗️ Architecture

```
Your Application
├── Extension 1: Drools (rules)
├── Extension 2: Kafka Reactive (messaging)
├── Extension 3: JSON Transform (mapping)
├── Extension 4: PostgreSQL (persistence)
└── Extension 5: Transaction Processor (domain)
     └── depends on Extension 4
```

**No inter-extension coupling** - Each extension is independent and reusable.

## 🚀 Build Order

Paths below are relative to the **repository root** (the directory that contains `build.sh` and the `quarkus-*-extension` folders).

**Recommended:** run the provided script from the repository root (forwards extra arguments to every Maven invocation):

```bash
./build.sh
# examples:
./build.sh -DskipTests
./build.sh --batch-mode
```

**Manual:** same order as `build.sh`—extensions 1–4 in any order among themselves, then extension 5, then the app:

```bash
# 1–4: independent of each other (shown in a fixed order for repeatability)
mvn clean install -f quarkus-drools-extension/pom.xml
mvn clean install -f quarkus-kafka-reactive-extension/pom.xml
mvn clean install -f quarkus-json-transform-extension/pom.xml
mvn clean install -f quarkus-postgres-extension/pom.xml

# 5: depends on Extension 4 (PostgreSQL)
mvn clean install -f quarkus-transaction-processor-extension/pom.xml

# 6: reference application (depends on all five extensions)
mvn clean install -f fraud-detection-service/pom.xml
```

## 📋 File Inventory

**Total: 27 files**
- 15 POMs (parent + runtime + deployment for each extension + app)
- 9 Java classes
- 1 Drools rules file
- 2 Documentation files

## ✨ Key Principles

✓ **KISS** - Each extension is simple and focused
✓ **SRP** - Each extension has one responsibility
✓ **Reusable** - Can use any extension in other projects
✓ **Testable** - Test each extension independently
✓ **Zero Coupling** - No inter-extension dependencies (except Transaction→Postgres)

## 🎓 How to Use

1. Read `FINAL-ARCHITECTURE.md` for complete overview
2. Read `POSTGRES-EXTENSION-GUIDE.md` for database extension details
3. Build extensions in order (see Build Order above)
4. Review `TransactionConsumer.java` to see how they compose

## 🔄 Dependency Graph

```
Drools Extension ───────────┐
Kafka Extension ────────────┼──→ Your App
JSON Transform Extension ───┤
PostgreSQL Extension ◄──┐   │
Transaction Processor ──┘───┘
```

## 📦 Maven Central

Each extension can be published independently:

```xml
<dependency>
    <groupId>com.neversoft.quarkus</groupId>
    <artifactId>quarkus-drools-extension</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then reused in any project.

## 🧪 Testing

Each extension includes test support. See individual extension READMEs or `POSTGRES-EXTENSION-GUIDE.md` for examples.

## 📖 Learn More

- `FINAL-ARCHITECTURE.md` - How the 5 extensions work together
- `POSTGRES-EXTENSION-GUIDE.md` - Deep guide for the PostgreSQL extension
- `COMPLETE-FILES-INDEX.md` - Complete file listing and purposes

---

**This is a production-ready, properly architected Quarkus extensions ecosystem.**
