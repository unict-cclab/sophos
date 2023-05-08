package it.unict;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClusterReconciler implements Reconciler<Cluster> {

  private static final Logger log = LoggerFactory.getLogger(ClusterReconciler.class);

  private final KubernetesClient client;

  @Inject
  ClusterGraphBuilder clusterGraphBuilder;

  @Inject
  NetworkMonitor networkMonitor;

  @Inject
  ResourceMonitor resourceMonitor;

  public ClusterReconciler(KubernetesClient client) {
    this.client = client;
  }

  @Override
  public UpdateControl<Cluster> reconcile(Cluster resource, Context context) {
    Map<String, String> nodeSelectorMap = resource.getSpec().getNodeSelector();
    if (nodeSelectorMap == null) {
      nodeSelectorMap = new HashMap<>();
    }

    List<Node> nodes = client.nodes()
            .withLabelSelector(new LabelSelectorBuilder().withMatchLabels(nodeSelectorMap).build())
            .list()
            .getItems();

    ClusterGraph clusterGraph = clusterGraphBuilder.buildClusterGraph(nodes, resource.getSpec().getMetricsRangeWidth());

    if(resource.getSpec().isResourceMonitorEnabled()) {
      log.info("Updating resource params for cluster {}", resource.getMetadata().getName());
      resourceMonitor.updateParams(clusterGraph);
    }

    if(resource.getSpec().isNetworkMonitorEnabled()) {
      log.info("Updating network params for cluster {}", resource.getMetadata().getName());
      networkMonitor.updateParams(clusterGraph);
    }

    clusterGraph.getClusterNodes().forEach(clusterNode -> {
      Node node = clusterNode.getNode();
      client.nodes().withName(node.getMetadata().getName()).patch(node);
    });

    return UpdateControl.<Cluster>noUpdate().rescheduleAfter(resource.getSpec().getRunPeriod(), TimeUnit.SECONDS);
  }
}

