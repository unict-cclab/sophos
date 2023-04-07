package it.unict;

import io.fabric8.kubernetes.api.model.Node;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ClusterGraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(ClusterGraphBuilder.class);

    @RestClient
    TelemetryService telemetryService;

    public ClusterGraph buildClusterGraph(List<Node> nodes) {
        ClusterGraph clusterGraph = new ClusterGraph();

        nodes.forEach(node -> {
            String clusterNodeName = node.getMetadata().getName();

            double cpuUsage = 0.0;
            try {
                cpuUsage = telemetryService
                        .getNodeCpuUsage(clusterNodeName)
                        .await().indefinitely();
            } catch (Exception e) {
                log.info(e.getMessage());
            }

            double allocatableCpu = Double.parseDouble(node
                    .getStatus()
                    .getAllocatable()
                    .get("cpu")
                    .getAmount());

            double availableCpu = (allocatableCpu - cpuUsage) * 1000;

            double availableMemory = 0.0;
            try {
                availableMemory = telemetryService
                        .getNodeAvailableMemory(clusterNodeName)
                        .await().indefinitely();
            } catch (Exception e) {
                log.info(e.getMessage());
            }

            Map<String,Double> latencies = new HashMap<>();
            try {
                latencies = telemetryService
                        .getNodeLatencies(clusterNodeName)
                        .await().indefinitely();
            } catch (Exception e) {
                log.info(e.getMessage());
            }

            clusterGraph.addClusterNode(node, clusterNodeName, availableCpu, availableMemory, latencies);
        });

        return clusterGraph;
    }
}
