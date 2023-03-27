IMG ?= ghcr.io/amarchese96/sophos-telemetry-service:latest
PROMETHEUS_ADDRESS ?= http://localhost:9090

run:
	PROMETHEUS_ADDRESS=${PROMETHEUS_ADDRESS} go run main.go

build:
	go build

docker-build:
	docker build -t ${IMG} .

docker-push:
	docker push ${IMG}
