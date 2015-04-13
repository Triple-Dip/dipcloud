package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.util.InMemoryConnectorPair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class DipTripleTest {

    private static final long MAX_WAIT_NANOS = (long) 10e9;
    private static final long SLEEP_MILLIS = 10;

    protected DipServer server;
    protected DipClient clientA;
    protected DipClient clientB;

    // Set up a server and two clients with in-memory connectors.
    @Before
    public void setUp() throws Exception {
        server = new DipServer(new Nimbase());

        InMemoryConnectorPair<Molecule> aToServer = new InMemoryConnectorPair<>();
        clientA = new DipClient(new Nimbase(), aToServer.getASendToB());
        server.addSession(aToServer.getBSendToA());

        InMemoryConnectorPair<Molecule> bToServer = new InMemoryConnectorPair<>();
        clientB = new DipClient(new Nimbase(), bToServer.getASendToB());
        server.addSession(bToServer.getBSendToA());
    }

    private void addTestAtomsToAll() {
        addAtomToAll(new Atom("A", 1, "A", 1, 1.0));
        addAtomToAll(new Atom("B", 2, "B", 2, 2.0));
    }

    private void addAtomToAll(Atom atom) {
        server.getNimbase().add(atom);
        clientA.getNimbase().add(atom);
        clientB.getNimbase().add(atom);
    }

    // Compare server and client Nimbases, one by one.
    private void makeSureAllNimbasesEqual() throws Exception {
        Atom[] serverAtoms = server.getNimbase().toOrderedArray(new Atom.IdIncreasing());
        Atom[] clientAAtoms = clientA.getNimbase().toOrderedArray(new Atom.IdIncreasing());
        Atom[] clientBAtoms = clientB.getNimbase().toOrderedArray(new Atom.IdIncreasing());

        assertArrayEquals(serverAtoms, clientAAtoms);
        assertArrayEquals(serverAtoms, clientBAtoms);
    }

    // Interrupt all sessions and make sure the threads died.
    private void stopAllSessions() throws Exception {
        server.stopSessions();
        clientA.stop();
        clientB.stop();

        Thread.sleep(SLEEP_MILLIS);

        assertTrue(clientA.getSession().inBoxIsStopped());
        assertTrue(clientA.getSession().outBoxIsStopped());
        assertTrue(clientB.getSession().inBoxIsStopped());
        assertTrue(clientB.getSession().outBoxIsStopped());

        for (Session<Molecule> session : server) {
            assertTrue(session.inBoxIsStopped());
            assertTrue(session.outBoxIsStopped());
        }
    }

    // Add some atoms to a client and make sure they propagate to all nodes.
    @Test
    public void testAddPropagation() throws Exception {
        Atom atomA = new Atom("A", 1, "A", 1, 1.0);
        Atom atomB = new Atom("B", 2, "B", 2, 2.0);
        Molecule toAdd = new Molecule("test add", atomA, atomB);

        server.startSessions();
        clientA.start();
        clientB.start();

        clientA.proposeAdd(toAdd);

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (System.nanoTime() < endTime
                && (server.getNimbase().size() < toAdd.size()
                || clientA.getNimbase().size() < toAdd.size()
                || clientB.getNimbase().size() < toAdd.size())) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // allow threads to finish
        stopAllSessions();

        assertEquals(toAdd.size(), server.getNimbase().size());
        assertEquals(atomA, server.getNimbase().get("A"));
        assertEquals(atomB, server.getNimbase().get("B"));
        makeSureAllNimbasesEqual();
    }

    // Add update some atoms from one client and make sure the update propagates to all nodes.
    @Test
    public void testUpdatePropagation() throws Exception {
        addTestAtomsToAll();
        Atom atomA = server.getNimbase().get("A");
        Atom atomB = server.getNimbase().get("B");

        Atom atomAUpdated = atomA.copy("Updated", atomA.getSequenceNumber() + 1);
        Atom atomBUpdated = atomB.copy("Updated", atomB.getSequenceNumber());
        Molecule toUpdate = new Molecule("test update", atomAUpdated, atomBUpdated);

        server.startSessions();
        clientA.start();
        clientB.start();

        clientA.proposeUpdate(toUpdate);

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (System.nanoTime() < endTime
                && (!server.getNimbase().get("A").equals(atomAUpdated)
                || !clientA.getNimbase().get("A").equals(atomAUpdated)
                || !clientB.getNimbase().get("A").equals(atomAUpdated))) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // allow threads to finish
        stopAllSessions();

        assertEquals(atomAUpdated, server.getNimbase().get("A"));
        assertNotEquals(atomBUpdated, server.getNimbase().get("B"));
        assertEquals(atomB, server.getNimbase().get("B"));
        makeSureAllNimbasesEqual();
    }

    // Remove an atom from one client and make the remove propagates to all nodes.
    @Test
    public void testRemovePropagation() throws Exception {
        addTestAtomsToAll();
        Atom atomA = server.getNimbase().get("A");
        Atom atomB = server.getNimbase().get("B");

        Molecule toRemove = new Molecule("test remove", atomA);

        server.startSessions();
        clientA.start();
        clientB.start();

        clientA.proposeRemove(toRemove);

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (System.nanoTime() < endTime
                && (server.getNimbase().size() > 1
                || clientA.getNimbase().size() > 1
                || clientB.getNimbase().size() > 1)) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // allow threads to finish
        stopAllSessions();

        assertEquals(1, server.getNimbase().size());
        assertEquals(atomB, server.getNimbase().get("B"));
        makeSureAllNimbasesEqual();
    }

    // Send a transient Atom from one client and make sure all nodes are informed.
    @Test
    public void testSendPropagation() throws Exception {
        DipListener serverListener = new DipListener();
        DipListener clientAListener = new DipListener();
        DipListener clientBListener = new DipListener();

        server.getIdListeners().registerListener("S", serverListener);
        clientA.getIdListeners().registerListener("S", clientAListener);
        clientB.getIdListeners().registerListener("S", clientBListener);

        Atom atomS = new Atom("S", 55, "S", 55, 55.0);

        Molecule toSend = new Molecule("test send", atomS);

        server.startSessions();
        clientA.start();
        clientB.start();

        clientA.proposeSend(toSend);

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (System.nanoTime() < endTime
                && (serverListener.getAtoms().isEmpty()
                || clientAListener.getAtoms().isEmpty()
                || clientBListener.getAtoms().isEmpty())) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // allow threads to finish
        stopAllSessions();

        assertEquals(1, serverListener.getAtoms().size());
        assertEquals(1, clientAListener.getAtoms().size());
        assertEquals(1, clientBListener.getAtoms().size());

        assertEquals(atomS, serverListener.getAtoms().get(0));
        assertEquals(atomS, clientAListener.getAtoms().get(0));
        assertEquals(atomS, clientBListener.getAtoms().get(0));

        assertEquals(0, server.getNimbase().size());
        makeSureAllNimbasesEqual();
    }

    // A listener to record transient "send" Atoms.
    private static class DipListener implements ScrudListener<Atom> {
        private final List<Atom> atoms = new ArrayList<>();

        @Override
        public void onAdded(Atom thing) {}

        @Override
        public synchronized void onUpdated(Atom thing) {}

        @Override
        public void onRemoved(Atom thing) {}

        @Override
        public synchronized void onSent(Atom thing) {
            atoms.add(thing);
        }

        public synchronized List<Atom> getAtoms() {
            return atoms;
        }
    }

    // Many async updates from all three nodes, make sure the right value wins in the end.
    @Test
    public void testConcurrentUpdates() throws Exception {
        // the atom that all three nodes will be updating
        Atom testAtom = new Atom("test", 0, "test", 0, 0.0);
        addAtomToAll(testAtom);

        // updaters race to make async updates at each node
        int updateCount = 100;
        Runnable serverUpdater = new Updater(testAtom.copy("server", 0), updateCount, server);
        Runnable clientAUpdater = new Updater(testAtom.copy("clientA", 0), updateCount, clientA);

        // clientB should get the last word
        Runnable clientBUpdater = new Updater(testAtom.copy("clientB", 0), updateCount + 1, clientB);

        server.startSessions();
        clientA.start();
        clientB.start();

        // Kick off the async update threads!
        Thread serverUpdaterThread = new Thread(clientBUpdater);
        Thread clientAUpdaterThread = new Thread(serverUpdater);
        Thread clientBUpdaterThread = new Thread(clientAUpdater);
        serverUpdaterThread.start();
        clientAUpdaterThread.start();
        clientBUpdaterThread.start();

        // Wait until updates have finished.
        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (System.nanoTime() < endTime
                && (server.getNimbase().get(testAtom.getId()).getSequenceNumber() < updateCount
                || clientA.getNimbase().get(testAtom.getId()).getSequenceNumber() < updateCount
                || clientB.getNimbase().get(testAtom.getId()).getSequenceNumber() < updateCount)) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // allow threads to finish
        stopAllSessions();

        // all Nimbases should see the last word from clientB
        assertEquals(updateCount, server.getNimbase().get(testAtom.getId()).getSequenceNumber());
        assertEquals("clientB", server.getNimbase().get(testAtom.getId()).getStringData());
        makeSureAllNimbasesEqual();

        // make sure the async updater threads will be cleaned up
        assertFalse(serverUpdaterThread.isAlive());
        assertFalse(clientAUpdaterThread.isAlive());
        assertFalse(clientBUpdaterThread.isAlive());
    }

    // Runnable to async pound on a Dip node.
    private static class Updater implements Runnable {
        private final DipAccess dip;
        private final Atom atom;
        private final int updateCount;

        private Updater(Atom atom, int updateCount, DipAccess dip) {
            this.dip = dip;
            this.atom = atom;
            this.updateCount = updateCount;
        }

        @Override
        public void run() {
            for (int i=0; i<updateCount; i++) {
                final Atom toUpdate = atom.copy(atom.getStringData(), i);
                dip.proposeUpdate(new Molecule("test update", toUpdate));
            }
        }
    }
}