package it.unict;

public class Link {

    private final String peerClusterNodeName;

    private final double latency;

    public Link(String neighborClusterNodeName, double latency) {
        this.peerClusterNodeName = neighborClusterNodeName;
        this.latency = latency;
    }

    public String getPeerClusterNodeName() {
        return peerClusterNodeName;
    }

    public Double getLatency() {
        return latency;
    }
}
