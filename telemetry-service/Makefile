IMG_NAME ?= ghcr.io/amarchese96/sophos-telemetry
IMG_TAG ?= latest
PROMETHEUS_ADDRESS ?= http://localhost:9090

run:
	PROMETHEUS_ADDRESS=${PROMETHEUS_ADDRESS} go run main.go

build:
	go build

docker-build:
	docker build -t ${IMG_NAME}:${IMG_TAG} .

docker-push:
	docker push ${IMG_NAME}:${IMG_TAG}
