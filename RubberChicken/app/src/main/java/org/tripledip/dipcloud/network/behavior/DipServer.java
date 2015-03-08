package org.tripledip.dipcloud.network.behavior;

import org.tripledip.dipcloud.local.behavior.SuperDip;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.contract.InBoxListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ben on 3/8/15.
 */
public class DipServer extends SuperDip implements InBoxListener<Molecule> {

    private final Set<Session<Molecule>> sessions;

    public DipServer(Crudable<Atom> nimbase) {
        super(nimbase);
        sessions = new HashSet<>();
    }

    public void addClientSession(Connector<Molecule> connector) {
        sessions.add(new Session<Molecule>(connector, this));
    }

    public void startClientSessions() {
        for (Session<Molecule> session : sessions) {
            session.startOutBox();
            session.startInBox();
        }
    }

    public void stopClients() {
        for (Session<Molecule> session : sessions) {
            session.stopOutBox();
            session.stopInBox();
        }
    }

    public void informClients(Molecule molecule) {
        for (Session<Molecule> session : sessions) {
            session.sendMessage(molecule);
        }
    }

    @Override
    protected void notifyAtomsAdded(Molecule molecule, Set<Atom> changed) {
        informClients(molecule);
        super.notifyAtomsAdded(molecule, changed);
    }

    @Override
    protected void notifyAtomsUpdated(Molecule molecule, Set<Atom> changed) {
        informClients(molecule);
        super.notifyAtomsUpdated(molecule, changed);
    }

    @Override
    protected void notifyAtomsRemoved(Molecule molecule, Set<Atom> changed) {
        informClients(molecule);
        super.notifyAtomsRemoved(molecule, changed);
    }

    @Override
    protected void notifyAtomsSent(Molecule molecule) {
        informClients(molecule);
        super.notifyAtomsSent(molecule);
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
