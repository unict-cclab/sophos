package it.unict;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeschedulerAppGroupReconciler implements Reconciler<DeschedulerAppGroup> {

  private static final Logger log = LoggerFactory.getLogger(DeschedulerAppGroupReconciler.class);
  private final KubernetesClient client;

  public DeschedulerAppGroupReconciler(KubernetesClient client) {
    this.client = client;
  }

  @Override
  public UpdateControl<DeschedulerAppGroup> reconcile(DeschedulerAppGroup resource, Context context) {
    log.info("Reconciling app group {}", resource.getSpec().getName());

    List<Deployment> deployments = client.apps()
            .deployments()
            .inNamespace(resource.getSpec().getNamespace())
            .withLabel("app-group", resource.getSpec().getName())
            .list()
            .getItems();

    int index = (int) Math.round(Math.random() * deployments.size());

    Deployment deployment = deployments.get(index);
    deployment.getSpec().getTemplate().getMetadata().getLabels().put(
            "random-value",
            String.valueOf(Math.random())
    );

    client.apps()
            .deployments()
            .inNamespace(deployment.getMetadata().getNamespace())
            .withName(deployment.getMetadata().getName())
            .patch(deployment);

    return UpdateControl.<DeschedulerAppGroup>noUpdate().rescheduleAfter(resource.getSpec().getRunPeriod(), TimeUnit.SECONDS);
  }
}

