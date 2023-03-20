package it.unict;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1alpha1")
@Group("unict.it")
public class Cluster extends CustomResource<ClusterSpec, ClusterStatus> implements Namespaced {}

