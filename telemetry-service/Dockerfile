FROM golang:1.18-buster AS build
WORKDIR /app
COPY go.mod ./
COPY go.sum ./
RUN go mod download
COPY . .
RUN go build -o /sophos-telemetry -buildvcs=false

FROM gcr.io/distroless/base-debian10
WORKDIR /
COPY --from=build /sophos-telemetry /sophos-telemetry
EXPOSE 8080
USER nonroot:nonroot
ENTRYPOINT ["/sophos-telemetry"]