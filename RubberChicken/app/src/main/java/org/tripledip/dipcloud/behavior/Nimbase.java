package org.tripledip.dipcloud.behavior;

import org.tripledip.dipcloud.contract.Crudable;
import org.tripledip.dipcloud.model.Atom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben on 2/18/2015.
 */
public class Nimbase implements Crudable<Atom> {

    private Map<String, Atom> atomsById;

    public Nimbase() {
        atomsById = new HashMap<>();
    }

    public Atom get(String id) {
        return atomsById.get(id);
    }

    public boolean add(Atom atom) {
        if (atomsById.containsKey(atom.getId())) {
            return update(atom);
        }
        atomsById.put(atom.getId(), atom);
        return true;
    }

    public boolean update(Atom incoming) {
        if (atomsById.containsKey(incoming.getId())) {
            final Atom existing = atomsById.get(incoming.getId());
            if (incoming.getTimeStamp() > existing.getTimeStamp()) {
                atomsById.put(incoming.getId(), incoming);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean remove(Atom atom) {
        if (atomsById.containsKey(atom.getId())) {
            atomsById.remove(atom.getId());
            return true;
        }
        return false;
    }

    public int size() {
        return atomsById.size();
    }

}
