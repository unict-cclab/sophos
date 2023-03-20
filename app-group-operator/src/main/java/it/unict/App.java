package it.unict;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.fabric8.kubernetes.api.model.apps.Deployment;

public class App {

    private final Deployment deployment;

    private final double cpuUsage;

    private final double memoryUsage;

    private final List<Channel> channels;

    public App(Deployment deployment, double cpuUsage, double memoryUsage, List<Channel> channels) {
        this.deployment = deployment;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.channels = channels;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public Map<String, Double> getTraffic() {
        return channels.stream().collect(Collectors.toMap(
                Channel::getPeerAppName,
                Channel::getTraffic,
                (v1, v2) -> v1
        ));
    }
}
