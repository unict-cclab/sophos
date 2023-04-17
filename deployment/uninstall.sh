#!/bin/bash

WORKDIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

helm uninstall sophos-scheduler -n scheduler-plugins

kubectl delete ns scheduler-plugins

kubectl delete -f $WORKDIR/descheduler-operator.yml

kubectl delete -f $WORKDIR/app-group-operator.yml

kubectl delete -f $WORKDIR/cluster-operator.yml

kubectl delete -f $WORKDIR/telemetry-service.yml

helm uninstall prometheus -n monitoring

istioctl uninstall --purge

kubectl delete ns istio-system

kubectl label namespace default istio-injection-

kubectl delete -f $WORKDIR/network-exporter.yml

kubectl delete ns monitoring