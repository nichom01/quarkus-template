package com.neversoft.quarkus.drools.config;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

@ConfigRoot(phase = io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.drools")
public interface DroolsConfig {
    String ruleFiles();
}
