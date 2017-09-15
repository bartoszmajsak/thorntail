/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.microprofile_metrics.runtime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.as.controller.ModelController;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * @author Heiko W. Rupp
 */
public class MetricsService implements Service<MetricsService> {

  private static Logger LOG = Logger.getLogger("org.wildfly.swarm.microprofile_metrics");
  private ExecutorService executorService;
  private ModelControllerClient controllerClient;

  public static final ServiceName SERVICE_NAME = ServiceName.of("swarm", "mp-metrics");

  private final InjectedValue<ServerEnvironment> serverEnvironmentValue = new InjectedValue<ServerEnvironment>();
  private final InjectedValue<ModelController> modelControllerValue = new InjectedValue<ModelController>();



  @Override
  public void start(StartContext context) throws StartException {
    executorService = Executors.newSingleThreadExecutor();
    controllerClient = modelControllerValue.getValue().createClient(executorService);

    BaseMetricWorker baseMetricWorker = BaseMetricWorker.create(controllerClient);
    baseMetricWorker.registerBaseMetrics();

    VendorMetricWorker vendorMetricWorker = VendorMetricWorker.create(controllerClient);
    vendorMetricWorker.registerVendorMetrics();
    LOG.info("MicroProfile-Metrics started");
  }

  @Override
  public void stop(StopContext context) {
    if (executorService != null) {
      executorService.shutdown();
    }
  }

  @Override
  public MetricsService getValue() throws IllegalStateException, IllegalArgumentException {
    return this;
  }

  /**
   * Register the metrics of the base scope with the system.
   */




  public Injector<ServerEnvironment> getServerEnvironmentInjector() {
      return this.serverEnvironmentValue;
  }

  public Injector<ModelController> getModelControllerInjector() {
      return this.modelControllerValue;
  }

}
