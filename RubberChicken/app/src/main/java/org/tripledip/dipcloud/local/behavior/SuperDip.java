package org.tripledip.dipcloud.local.behavior;

import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.Set;

/**
 * Created by Ben on 2/23/15.
 */
public class SuperDip {
    private final Crudable<Atom> nimbase;
    private final Atomizer atomizer;
    private final ScrudNotifier<Atom> idListeners;
    private final ScrudNotifier<Molecule> channelListeners;

    public SuperDip(Crudable<Atom> nimbase) {
        this.nimbase = nimbase;
        this.atomizer = new Atomizer(nimbase);
        this.idListeners = new ScrudNotifier<>();
        this.channelListeners = new ScrudNotifier<>();
    }

    public void add(Molecule molecule) {
        molecule.setAction(Molecule.Action.ADD);
        Set<Atom> changed = atomizer.add(molecule);
        if (!changed.isEmpty()) {
            molecule.addOrReplaceAll(changed);
            notifyAtomsAdded(molecule, changed);
        }
    }

    protected void notifyAtomsAdded(Molecule molecule, Set<Atom> changed) {
        for (Atom atom : changed) {
            idListeners.notifyAdded(atom.getId(), atom);
        }
        channelListeners.notifyAdded(molecule.getChannel(), molecule);
    }

    public void update(Molecule molecule) {
        molecule.setAction(Molecule.Action.UPDATE);
        Set<Atom> changed = atomizer.update(molecule);
        if (!changed.isEmpty()) {
            molecule.addOrReplaceAll(changed);
            notifyAtomsUpdated(molecule, changed);
        }
    }

    protected void notifyAtomsUpdated(Molecule molecule, Set<Atom> changed) {
        for (Atom atom : changed) {
            idListeners.notifyUpdated(atom.getId(), atom);
        }
        channelListeners.notifyUpdated(molecule.getChannel(), molecule);
    }

    public void remove(Molecule molecule) {
        molecule.setAction(Molecule.Action.REMOVE);
        Set<Atom> changed = atomizer.remove(molecule);
        if (!changed.isEmpty()) {
            molecule.addOrReplaceAll(changed);
            notifyAtomsRemoved(molecule, changed);
        }
    }

    protected void notifyAtomsRemoved(Molecule molecule, Set<Atom> changed) {
        for (Atom atom : changed) {
            idListeners.notifyRemoved(atom.getId(), atom);
        }
        channelListeners.notifyRemoved(molecule.getChannel(), molecule);
    }


    public void send(Molecule molecule) {
        molecule.setAction(Molecule.Action.SEND);
        notifyAtomsSent(molecule);
    }

    protected void notifyAtomsSent(Molecule molecule) {
        for (Atom atom : molecule) {
            idListeners.notifySent(atom.getId(), atom);
        }
        channelListeners.notifySent(molecule.getChannel(), molecule);
    }

    public Crudable<Atom> getNimbase() {
        return nimbase;
    }

    public ScrudNotifier<Atom> getIdListeners() {
        return idListeners;
    }

    public ScrudNotifier<Molecule> getChannelListeners() {
        return channelListeners;
    }
}
