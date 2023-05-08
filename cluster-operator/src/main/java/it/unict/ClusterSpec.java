package it.unict;

import java.util.Map;

public class ClusterSpec {

    private Map<String, String> nodeSelector;

    private Boolean resourceMonitorEnabled;

    private Boolean networkMonitorEnabled;

    private Integer runPeriod;

    private String metricsRangeWidth;

    public Map<String, String> getNodeSelector(){
        return nodeSelector;
    }

    public Boolean isResourceMonitorEnabled() {
        return resourceMonitorEnabled;
    }

    public Boolean isNetworkMonitorEnabled() {
        return networkMonitorEnabled;
    }

    public Integer getRunPeriod() { return runPeriod; }

    public String getMetricsRangeWidth() { return metricsRangeWidth; }
}
