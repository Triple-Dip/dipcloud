package org.tripledip.dipcloud.local.behavior;

import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.model.Atom;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by Ben on 2/18/2015.
 */
public class Nimbase implements Crudable<Atom> {

    private long maxSequenceNumber;

    private final Map<String, Atom> atomsById = new HashMap<>();

    @Override
    public synchronized Atom get(String id) {
        return atomsById.get(id);
    }

    @Override
    public synchronized boolean add(Atom atom) {
        if (atomsById.containsKey(atom.getId())) {
            return update(atom);
        }
        atomsById.put(atom.getId(), atom);
        updateMaxSequenceNumber(atom.getSequenceNumber());
        return true;
    }

    @Override
    public synchronized boolean update(Atom incoming) {
        if (atomsById.containsKey(incoming.getId())) {
            final Atom existing = atomsById.get(incoming.getId());
            if (incoming.getSequenceNumber() > existing.getSequenceNumber()) {
                atomsById.put(incoming.getId(), incoming);
                updateMaxSequenceNumber(incoming.getSequenceNumber());
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public synchronized boolean remove(Atom atom) {
        if (atomsById.containsKey(atom.getId())) {
            atomsById.remove(atom.getId());
            updateMaxSequenceNumber(atom.getSequenceNumber());
            return true;
        }
        return false;
    }

    @Override
    public synchronized int size() {
        return atomsById.size();
    }

    @Override
    public synchronized long nextSequenceNumber() {
        return maxSequenceNumber + 1;
    }

    @Override
    public synchronized Collection<Atom> fillCollection(Collection<Atom> collection){
        collection.addAll(atomsById.values());
        return collection;
    }

    @Override
    public synchronized Atom[] toOrderedArray(Comparator<Atom> comparator) {
        return fillCollection(new TreeSet<Atom>(comparator)).toArray(new Atom[size()]);
    }

    private void updateMaxSequenceNumber(long number) {
        if (number > maxSequenceNumber) {
            maxSequenceNumber = number;
        }
    }
}
