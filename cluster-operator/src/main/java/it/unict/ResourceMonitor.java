package it.unict;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResourceMonitor {

    public void updateParams(ClusterGraph clusterGraph) {
        clusterGraph.getClusterNodes().forEach(clusterNode -> {
            clusterNode.getNode().getMetadata().getAnnotations().put(
                    "available-cpu",
                    String.valueOf(clusterNode.getAvailableCpu())
            );

            clusterNode.getNode().getMetadata().getAnnotations().put(
                    "available-memory",
                    String.valueOf(clusterNode.getAvailableMemory())
            );
        });
    }
}
