package org.tripledip.dipcloud.local.model;

import java.util.Comparator;

/**
 * Created by Wolfe on 2/18/2015.
 */
public final class Atom {

    private final String id;
    private final long sequenceNumber;
    private final String stringData;
    private final int intData;
    private final double doubleData;

    public Atom(String id, long sequenceNumber, String stringData, int intData, double doubleData) {
        this.id = id;
        this.sequenceNumber = sequenceNumber;
        this.stringData = stringData;
        this.intData = intData;
        this.doubleData = doubleData;
    }

    public Atom(String id, long sequenceNumber, String stringData) {
        this.id = id;
        this.sequenceNumber = sequenceNumber;
        this.stringData = stringData;
        this.intData = 0;
        this.doubleData = 0.0;
    }

    public Atom(String id, long sequenceNumber, int intData) {
        this.id = id;
        this.sequenceNumber = sequenceNumber;
        this.stringData = null;
        this.intData = intData;
        this.doubleData = 0.0;
    }

    public Atom(String id, long sequenceNumber, double doubleData) {
        this.id = id;
        this.sequenceNumber = sequenceNumber;
        this.stringData = null;
        this.intData = 0;
        this.doubleData = doubleData;
    }

    public String getId() {
        return id;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
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

    public Atom copy(String stringData, long sequenceNumber) {
        return new Atom(this.id, sequenceNumber, stringData, this.intData, this.doubleData);
    }

    public Atom copy(int intData, long sequenceNumber) {
        return new Atom(this.id, sequenceNumber, this.stringData, intData, this.doubleData);
    }

    public Atom copy(double doubleData, long sequenceNumber) {
        return new Atom(this.id, sequenceNumber, this.stringData, this.intData, doubleData);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + 31 * Long.valueOf(sequenceNumber).hashCode();
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
                && otherAtom.sequenceNumber == this.sequenceNumber
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

    public static class SequenceNumberIncreasing implements Comparator<Atom> {
        @Override
        public int compare(Atom lhs, Atom rhs) {
            return Long.valueOf(lhs.sequenceNumber).compareTo(Long.valueOf(rhs.sequenceNumber));
        }
    }
}
