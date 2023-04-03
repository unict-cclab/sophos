## Sophos installation


Move to the deployment directory:

```
cd deployment
```

Create kind cluster (not required if you have just a running Kubernetes cluster):

```
kind create cluster --config kind-cluster.yml
```

Create monitoring namespace:

```
kubectl create ns monitoring
```

Install network-exporter:

```
kubectl apply -f network-exporter.yml
```

Install Istio:

```
istioctl install --set profile=demo -y
```

Label default namespace:

```
kubectl label namespace default istio-injection=enabled
```

Install and configure prometheus:

```
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts

helm install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace --values prometheus/values.yml --version 45.7.1

kubectl apply -f prometheus/config.yml
```

Install telemetry-service:

```
kubectl apply -f telemetry-service.yml
```

Install cluster-operator:

```
kubectl apply -f cluster-operator.yml
```

Install app-group-operator:

```
kubectl apply -f app-group-operator.yml
```

Install scheduler:

```
helm install sophos-scheduler ../scheduler-plugins/manifests/install/charts/as-a-second-scheduler --values scheduler.yml -n scheduler-plugins --create-namespace
```

Clean up:

```
helm uninstall sophos-scheduler -n scheduler-plugins

kubectl delete ns scheduler-plugins

kubectl delete -f app-group-operator.yml

kubectl delete -f cluster-operator.yml

kubectl delete -f telemetry-service.yml

helm uninstall prometheus -n monitoring

kubectl delete ns monitoring

istioctl manifest generate --set profile=demo | kubectl delete --ignore-not-found=true -f -

kubectl delete ns istio-system

kubectl label namespace default istio-injection-

kubectl delete -f network-exporter.yml

kubectl delete ns monitoring
```