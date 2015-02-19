package org.tripledip.dipcloud;

/**
 * Created by Wolfe on 2/18/2015.
 */
public final class Atom {

    private final String id;
    private final String channel;
    private final long timeStamp;
    private final int action;

    private final String stringData;
    private final int intData;
    private final double doubleData;

    public Atom(String id, String channel, long timeStamp, int action, String stringData, int intData, double doubleData) {
        this.id = id;
        this.channel = channel;
        this.timeStamp = timeStamp;
        this.action = action;
        this.stringData = stringData;
        this.intData = intData;
        this.doubleData = doubleData;
    }

    public String getId() {
        return id;
    }

    public String getChannel() {
        return channel;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getAction() {
        return action;
    }

    public String getStringData() {
        return stringData;
    }

    public int getIntData() {
        return intData;
    }

    public double getDoubleData() {
        return doubleData;
    }
}
