package com.neversoft.quarkus.postgres.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * PostgreSQL persistence configuration root.
 *
 * Configuration properties:
 * quarkus.postgres.database=quarkus_db
 * quarkus.postgres.schema=public
 * quarkus.postgres.enable-logging=false
 * quarkus.postgres.max-pool-size=20
 * quarkus.postgres.min-pool-size=5
 */
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.postgres")
public interface PersistenceConfig {

    /**
     * Database name.
     * Default: quarkus_db
     */
    @WithDefault("quarkus_db")
    String database();

    /**
     * Database schema.
     * Default: public
     */
    @WithDefault("public")
    String schema();

    /**
     * Enable SQL logging.
     * Default: false
     */
    @WithDefault("false")
    boolean enableLogging();

    /**
     * Maximum database connection pool size.
     * Default: 20
     */
    @WithDefault("20")
    int maxPoolSize();

    /**
     * Minimum database connection pool size.
     * Default: 5
     */
    @WithDefault("5")
    int minPoolSize();

    /**
     * Enable automatic schema generation on startup.
     * Default: false (set to drop-and-create or update in application.properties)
     */
    @WithDefault("false")
    boolean autoGenerateSchema();
}
