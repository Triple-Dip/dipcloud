package org.tripledip.dipcloud.local.model;

import java.util.Comparator;

/**
 * Created by Wolfe on 2/18/2015.
 */
public final class Atom {

    private final String id;
    private final long timeStamp;
    private final String stringData;
    private final int intData;
    private final double doubleData;

    public Atom(String id, long timeStamp, String stringData, int intData, double doubleData) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.stringData = stringData;
        this.intData = intData;
        this.doubleData = doubleData;
    }

    public String getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
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

    public Atom copy(String stringData, long timeStamp) {
        return new Atom(this.id, timeStamp, stringData, this.intData, this.doubleData);
    }

    public Atom copy(int intData, long timeStamp) {
        return new Atom(this.id, timeStamp, this.stringData, intData, this.doubleData);
    }

    public Atom copy(double doubleData, long timeStamp) {
        return new Atom(this.id, timeStamp, this.stringData, this.intData, doubleData);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + 31 * Long.valueOf(timeStamp).hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Atom)) {
            return false;
        }

        final Atom otherAtom = (Atom) other;
        return otherAtom.id.equals(this.id)
                && otherAtom.timeStamp == this.timeStamp
                && otherAtom.stringData.equals(this.stringData)
                && otherAtom.intData == this.intData
                && otherAtom.doubleData == this.doubleData;
    }

    public static class IdIncreasing implements Comparator<Atom> {
        @Override
        public int compare(Atom lhs, Atom rhs) {
            return lhs.id.compareTo(rhs.id);
        }
    }

    public static class TimestampIncreasing implements Comparator<Atom> {
        @Override
        public int compare(Atom lhs, Atom rhs) {
            return Long.valueOf(lhs.timeStamp).compareTo(Long.valueOf(rhs.timeStamp));
        }
    }
}
