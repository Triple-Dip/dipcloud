package org.tripledip.dipcloud.behavior;

import org.tripledip.dipcloud.contract.Crudable;
import org.tripledip.dipcloud.model.Atom;
import org.tripledip.dipcloud.model.Molecule;
import org.tripledip.dipcloud.protocol.ClientSession;
import org.tripledip.dipcloud.protocol.ServerSession;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Ben on 2/23/15.
 */
public class DipCloud {

    private final Crudable<Atom> nimbase;
    private final Atomizer atomizer;
    private final ScrudNotifier<Atom> idListeners;
    private final ScrudNotifier<Molecule> channelListeners;
    private final ClientSession clientSession;
    private final Set<ServerSession> serverSessions;

    public DipCloud(Crudable<Atom> nimbase, ClientSession clientSession) {
        this.nimbase = nimbase;
        this.atomizer = new Atomizer(nimbase);
        this.idListeners = new ScrudNotifier<>();
        this.channelListeners = new ScrudNotifier<>();
        this.clientSession = clientSession;
        this.serverSessions = new HashSet<>();

        // TODO: what kind of client session init?
    }

    public void add(Molecule molecule) {
        molecule.setAction(Molecule.Action.ADD);
        Set<Atom> changed = atomizer.add(molecule);
        if (!changed.isEmpty()) {
            for (Atom atom : changed) {
                idListeners.notifyAdded(atom.getId(), atom);
            }
            channelListeners.notifyAdded(molecule.getChannel(), molecule);
        }
    }

    public void update(Molecule molecule) {
        molecule.setAction(Molecule.Action.UPDATE);
        Set<Atom> changed = atomizer.update(molecule);
        if (!changed.isEmpty()) {
            for (Atom atom : changed) {
                idListeners.notifyUpdated(atom.getId(), atom);
            }
            channelListeners.notifyUpdated(molecule.getChannel(), molecule);
        }
    }

    public void remove(Molecule molecule) {
        molecule.setAction(Molecule.Action.REMOVE);
        Set<Atom> changed = atomizer.remove(molecule);
        if (!changed.isEmpty()) {
            for (Atom atom : changed) {
                idListeners.notifyRemoved(atom.getId(), atom);
            }
            channelListeners.notifyRemoved(molecule.getChannel(), molecule);
        }
    }

    public void send(Molecule molecule) {
        molecule.setAction(Molecule.Action.SEND);
        Collection<Atom> sent = molecule.fillCollection(new HashSet<Atom>());
        for (Atom atom : sent) {
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

    public ClientSession getClientSession() {
        return clientSession;
    }

    public boolean addServerSession(ServerSession serverSession) {
        // TODO: what kind of server session init?
        return serverSessions.add(serverSession);
    }

    public Iterator<ServerSession> serverSessionIterator() {
        return serverSessions.iterator();
    }
}
