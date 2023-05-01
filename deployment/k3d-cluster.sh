#!/bin/bash

command=$1

if [ $# -lt 1 ]
then
    echo "Specify the command to run"
    exit 1
fi

WORKDIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

case $command in
    "start")
        k3d cluster create --config $WORKDIR/k3d-cluster.yml
        ;;
    "delete")
        k3d cluster delete --config $WORKDIR/k3d-cluster.yml
        ;;
     *)
        echo "${command} is not a valid command"
        exit 1
        ;;
esac