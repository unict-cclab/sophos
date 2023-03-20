package it.unict;

public class Channel {

    private final String peerAppName;

    private final double traffic;

    public Channel(String peerAppName, double traffic) {
        this.peerAppName = peerAppName;
        this.traffic = traffic;
    }

    public String getPeerAppName() {
        return peerAppName;
    }

    public double getTraffic() {
        return traffic;
    }
}
