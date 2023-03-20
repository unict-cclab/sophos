package it.unict;

import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.fabric8.kubernetes.api.model.apps.Deployment;

@ApplicationScoped
public class AppGroupGraphBuilder {
    
    @RestClient
    TelemetryService telemetryService;
    
    public AppGroupGraph buildAppGroupGraph(String appGroupName, List<Deployment> deployments) {
        AppGroupGraph appGroupGraph = new AppGroupGraph();
    
        deployments.forEach(deployment -> {
            String appName = deployment
              .getMetadata()
              .getLabels()
              .get("app");
    
            double cpuUsage = telemetryService
              .getAppCpuUsage(appGroupName, appName)
              .await().indefinitely();
    
            double memoryUsage = telemetryService
              .getAppMemoryUsage(appGroupName, appName)
              .await()
              .indefinitely();
    
            Map<String,Double> traffic = telemetryService
              .getAppTraffic(appGroupName, appName, "all")
              .await()
              .indefinitely();
    
            appGroupGraph.addApp(deployment, cpuUsage, memoryUsage, traffic);
        });
    
        return appGroupGraph;
    }
}
