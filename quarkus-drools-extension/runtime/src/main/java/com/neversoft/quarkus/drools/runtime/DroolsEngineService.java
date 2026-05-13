package com.neversoft.quarkus.drools.runtime;

import com.neversoft.quarkus.drools.config.DroolsConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.drools.io.ClassPathResource;
import org.drools.model.codegen.ExecutableModelProject;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieSession;

@ApplicationScoped
public class DroolsEngineService {

    private KieSession kieSession;

    @Inject
    public void init(DroolsConfig config) {
        initializeKieSession(config.ruleFiles());
    }

    private void initializeKieSession(String ruleFiles) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();

        // Load rule files
        String[] files = ruleFiles.split(",");
        for (String file : files) {
            kfs.write(new ClassPathResource(file.trim()));
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
        kieBuilder.buildAll(ExecutableModelProject.class);

        if (kieBuilder.getResults().hasMessages()) {
            throw new IllegalStateException("Unable to compile Drools rules: " +
                    kieBuilder.getResults().getMessages());
        }

        KieModule kieModule = kieBuilder.getKieModule();
        KieBase kieBase = kieServices.newKieContainer(kieModule.getReleaseId()).getKieBase();
        this.kieSession = kieBase.newKieSession();
    }

    public <T> T execute(T fact) {
        kieSession.insert(fact);
        kieSession.fireAllRules();
        kieSession.delete(kieSession.getFactHandle(fact));
        return fact;
    }
}
