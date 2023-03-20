package it.unict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.apps.Deployment;

public class AppGroupGraph {
    private final List<App> apps;

    AppGroupGraph() {
        apps = new ArrayList<>();
    }

    public List<App> getApps() {
        return apps;
    }

    public void addApp(Deployment deployment, double cpuUsage, double memoryUsage, Map<String, Double> traffic) {
        List<Channel> channels = traffic.entrySet()
                .stream()
                .map((entry) -> new Channel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        apps.add(new App(deployment, cpuUsage, memoryUsage, channels));
    }
}
