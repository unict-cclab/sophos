#!/bin/bash

WORKDIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

kubectl create ns monitoring

kubectl apply -f $WORKDIR/network-exporter.yml

istioctl install --set profile=default -y

kubectl label namespace default istio-injection=enabled

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts

helm install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace --values $WORKDIR/prometheus/values.yml --version 45.7.1

kubectl apply -f $WORKDIR/prometheus/config.yml

kubectl apply -f $WORKDIR/telemetry-service.yml

kubectl apply -f $WORKDIR/cluster-operator.yml

kubectl apply -f $WORKDIR/app-group-operator.yml

kubectl apply -f $WORKDIR/descheduler-operator.yml

helm install sophos-scheduler $WORKDIR/../scheduler-plugins/manifests/install/charts/as-a-second-scheduler --values $WORKDIR/scheduler.yml -n scheduler-plugins --create-namespace
