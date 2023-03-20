package it.unict;

public class AppGroupSpec {

    private String name;

    private String namespace;

    private Boolean resourceMonitorEnabled;

    private Boolean communicationMonitorEnabled;

    private Integer runPeriod;

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
}
