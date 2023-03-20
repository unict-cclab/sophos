package it.unict;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResourceMonitor {
    public void updateParams(AppGroupGraph appGroupGraph) {
        appGroupGraph.getApps().forEach(app -> {
            app.getDeployment().getMetadata().getLabels().put(
                    "cpu-usage",
                    String.valueOf(app.getCpuUsage())
            );

            app.getDeployment().getMetadata().getLabels().put(
                    "memory-usage",
                    String.valueOf(app.getMemoryUsage())
            );
        });
    }
}
