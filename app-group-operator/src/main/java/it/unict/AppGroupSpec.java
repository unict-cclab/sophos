package it.unict;

public class AppGroupSpec {

    private String name;

    private String namespace;

    private Boolean resourceMonitorEnabled;

    private Boolean communicationMonitorEnabled;

    private Integer runPeriod;

    private String metricsRangeWidth;

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public Boolean isResourceMonitorEnabled() {
        return resourceMonitorEnabled;
    }

    public Boolean isCommunicationMonitorEnabled() { return communicationMonitorEnabled; }

    public Integer getRunPeriod() { return runPeriod; }

    public String getMetricsRangeWidth() { return metricsRangeWidth; }
}
