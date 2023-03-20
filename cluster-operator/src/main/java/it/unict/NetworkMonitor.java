package it.unict;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NetworkMonitor {

    public void updateParams(ClusterGraph clusterGraph) {
        clusterGraph.getClusterNodes().forEach(clusterNode -> {
            clusterNode.getLatencies().forEach((peerClusterNodeName, latency) -> clusterNode.getNode()
                    .getMetadata()
                    .getLabels()
                    .put("network-latency." + peerClusterNodeName, String.valueOf(latency)));

            clusterNode.getNode()
                    .getMetadata()
                    .getLabels()
                    .put("network-latency." + clusterNode.getName(), String.valueOf(0.0)
            );
        });
    }
}
