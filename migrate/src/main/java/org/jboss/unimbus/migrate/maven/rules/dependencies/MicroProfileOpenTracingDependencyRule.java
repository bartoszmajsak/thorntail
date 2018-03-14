package org.jboss.unimbus.migrate.maven.rules.dependencies;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.unimbus.migrate.maven.DependencyRemoveRule;

/**
 * Created by bob on 3/13/18.
 */
@ApplicationScoped
public class MicroProfileOpenTracingDependencyRule extends DependencyRemoveRule {

    protected MicroProfileOpenTracingDependencyRule(String groupId, String artifactId) {
        super("org.wildfly.swarm", "microprofile-opentracing");
    }
}