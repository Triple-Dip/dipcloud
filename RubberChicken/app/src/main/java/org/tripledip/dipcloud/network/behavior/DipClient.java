package org.tripledip.dipcloud.network.behavior;

import org.tripledip.dipcloud.local.behavior.SuperDip;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.contract.InBoxListener;

/**
 * Created by Ben on 3/8/15.
 */
public class DipClient extends SuperDip implements InBoxListener<Molecule> {

    private final Session<Molecule> session;

    public DipClient(Crudable<Atom> nimbase, Connector<Molecule> connector) {
        super(nimbase);
        session = new Session<>(connector, this);
    }

    public void start() {
        session.startOutBox();
        session.startInBox();
    }

    public void stop() {
        session.stopOutBox();
        session.stopInBox();
    }

    public void proposeAdd(Molecule molecule) {
        molecule.setAction(Molecule.Action.ADD);
        session.sendMessage(molecule);
    }

    public void proposeUpdate(Molecule molecule) {
        molecule.setAction(Molecule.Action.UPDATE);
        session.sendMessage(molecule);
    }

    public void proposeRemove(Molecule molecule) {
        molecule.setAction(Molecule.Action.REMOVE);
        session.sendMessage(molecule);
    }

    public void proposeSend(Molecule molecule) {
        molecule.setAction(Molecule.Action.SEND);
        session.sendMessage(molecule);
    }

    @Override
    public void onInboxItemArrived(Molecule item) {
        switch (item.getAction()) {
            case ADD:
                add(item);
                break;
            case UPDATE:
                update(item);
                break;
            case REMOVE:
                remove(item);
                break;
            case SEND:
                send(item);
                break;
        }
    }
}
