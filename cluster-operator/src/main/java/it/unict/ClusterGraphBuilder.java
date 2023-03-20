package it.unict;

import io.fabric8.kubernetes.api.model.Node;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ClusterGraphBuilder {

    @RestClient
    TelemetryService telemetryService;

    public ClusterGraph buildClusterGraph(List<Node> nodes) {
        ClusterGraph clusterGraph = new ClusterGraph();

        nodes.forEach(node -> {
            String clusterNodeName = node.getMetadata().getName();

            double availableCpuPercentage = telemetryService
                    .getNodeAvailableCpu(clusterNodeName)
                    .await().indefinitely();

            double allocatableCpu = Double.parseDouble(node
                    .getStatus()
                    .getAllocatable()
                    .get("cpu")
                    .getAmount());

            double availableCpu = availableCpuPercentage * allocatableCpu * 1000;

            double availableMemory = telemetryService
                    .getNodeAvailableMemory(clusterNodeName)
                    .await()
                    .indefinitely();

            Map<String,Double> latencies = telemetryService
                    .getNodeLatencies(clusterNodeName)
                    .await()
                    .indefinitely();

            clusterGraph.addClusterNode(node, clusterNodeName, availableCpu, availableMemory, latencies);
        });

        return clusterGraph;
    }
}
