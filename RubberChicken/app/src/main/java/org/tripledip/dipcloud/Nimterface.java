package org.tripledip.dipcloud;

/**
 * Created by Ben on 2/21/15.
 */
public class Nimterface implements Scrudable<Molecule> {

    private Crudable<Atom> nimbase;

    public Nimterface(Crudable nimbase) {
        this.nimbase = nimbase;
    }

    @Override
    public boolean send(Molecule thing) {
        return false;
    }

    @Override
    public Molecule get(String id) {
        Atom atom = nimbase.get(id);
        if (null == atom) {
            return null;
        }
        return new Molecule(Molecule.DEFAULT_CHANNEL, Molecule.ACTION_GET, atom);
    }

    @Override
    public boolean add(Molecule thing) {
        boolean anythingHappened = false;
        for (Atom atom : thing) {
            anythingHappened |= nimbase.add(atom);
        }
        return anythingHappened;
    }

    @Override
    public boolean update(Molecule thing) {
        boolean anythingHappened = false;
        for (Atom atom : thing) {
            anythingHappened |= nimbase.update(atom);
        }
        return anythingHappened;
    }

    @Override
    public boolean remove(Molecule thing) {
        boolean anythingHappened = false;
        for (Atom atom : thing) {
            anythingHappened |= nimbase.remove(atom);
        }
        return anythingHappened;
    }

    @Override
    public int size() {
        return nimbase.size();
    }
}
