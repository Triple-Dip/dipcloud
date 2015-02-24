package org.tripledip.dipcloud.behavior;

import org.tripledip.dipcloud.contract.Crudable;
import org.tripledip.dipcloud.model.Atom;
import org.tripledip.dipcloud.model.Molecule;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ben on 2/21/15.
 */
public class Atomizer {

    private Crudable<Atom> nimbase;

    public Atomizer(Crudable nimbase) {
        this.nimbase = nimbase;
    }

    public Set<Atom> add(Molecule molecule) {
        Set<Atom> changedAtoms = new HashSet<Atom>();
        for (Atom atom : molecule) {
            if (nimbase.add(atom)) {
                changedAtoms.add(atom);
            }
        }
        return changedAtoms;
    }

    public Set<Atom> update(Molecule molecule) {
        Set<Atom> changedAtoms = new HashSet<Atom>();
        for (Atom atom : molecule) {
            if (nimbase.update(atom)) {
                changedAtoms.add(atom);
            }
        }
        return changedAtoms;
    }

    public Set<Atom> remove(Molecule molecule) {
        Set<Atom> changedAtoms = new HashSet<Atom>();
        for (Atom atom : molecule) {
            if (nimbase.remove(atom)) {
                changedAtoms.add(atom);
            }
        }
        return changedAtoms;
    }
}
