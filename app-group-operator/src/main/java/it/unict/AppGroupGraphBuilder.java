package it.unict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppGroupGraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(AppGroupGraphBuilder.class);
    
    @RestClient
    TelemetryService telemetryService;
    
    public AppGroupGraph buildAppGroupGraph(String appGroupName, List<Deployment> deployments) {
        AppGroupGraph appGroupGraph = new AppGroupGraph();
    
        deployments.forEach(deployment -> {
            String appName = deployment
              .getMetadata()
              .getLabels()
              .get("app");

            double cpuUsage = 0.0;
            try {
                cpuUsage = telemetryService
                        .getAppCpuUsage(appGroupName, appName)
                        .await().indefinitely();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
    
            double memoryUsage = 0.0;
            try {
                memoryUsage = telemetryService
                        .getAppMemoryUsage(appGroupName, appName)
                        .await().indefinitely();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
    
            Map<String,Double> traffic = new HashMap<>();
            try {
                traffic = telemetryService
                        .getAppTraffic(appGroupName, appName, "all")
                        .await().indefinitely();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
    
            appGroupGraph.addApp(deployment, cpuUsage, memoryUsage, traffic);
        });
    
        return appGroupGraph;
    }
}
