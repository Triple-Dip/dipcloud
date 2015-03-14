package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.util.InMemoryConnectorPair;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class DipTripleTest {

    private static final long MAX_WAIT_NANOS = (long) 2e9;
    private static final long SLEEP_MILLIS = 10;

    private DipServer server;
    private DipClient clientA;
    private DipClient clientB;

    @Before
    public void setUp() throws Exception {
        server = new DipServer(new Nimbase());

        InMemoryConnectorPair<Molecule> aToServer = new InMemoryConnectorPair<>();
        clientA = new DipClient(new Nimbase(), aToServer.getASendToB());
        server.addClientSession(aToServer.getBSendToA());

        InMemoryConnectorPair<Molecule> bToServer = new InMemoryConnectorPair<>();
        clientB = new DipClient(new Nimbase(), bToServer.getASendToB());
        server.addClientSession(bToServer.getBSendToA());
    }

    private void addTestAtomsToAll() {
        Atom atomA = new Atom("A", 1, "A", 1, 1.0);
        Atom atomB = new Atom("B", 2, "B", 2, 2.0);

        server.getNimbase().add(atomA);
        server.getNimbase().add(atomB);

        clientA.getNimbase().add(atomA);
        clientA.getNimbase().add(atomB);

        clientB.getNimbase().add(atomA);
        clientB.getNimbase().add(atomB);
    }

    private void compareAllNimbases() {
        Atom[] serverAtoms = server.getNimbase().toOrderedArray(new Atom.IdIncreasing());
        Atom[] clientAAtoms = clientA.getNimbase().toOrderedArray(new Atom.IdIncreasing());
        Atom[] clientBAtoms = clientB.getNimbase().toOrderedArray(new Atom.IdIncreasing());

        assertArrayEquals(serverAtoms, clientAAtoms);
        assertArrayEquals(serverAtoms, clientBAtoms);
    }

    @Test
    public void testAddPropagation() throws Exception {
        Atom atomA = new Atom("A", 1, "A", 1, 1.0);
        Atom atomB = new Atom("B", 2, "B", 2, 2.0);
        Molecule toAdd = new Molecule("test add", atomA, atomB);

        server.startClientSessions();
        clientA.start();
        clientB.start();

        clientA.proposeAdd(toAdd);

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (server.getNimbase().size() < toAdd.size()
                && clientA.getNimbase().size() < toAdd.size()
                && clientB.getNimbase().size() < toAdd.size()
                && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }

        compareAllNimbases();

        // allow threads to finish
        server.stopClientSessions();
        clientA.stop();
        clientB.stop();
        // TODO: assert everyone stopped
    }

    @Test
    public void testUpdatePropagation() throws Exception {
        addTestAtomsToAll();
        Atom atomA = server.getNimbase().get("A");
        Atom atomB = server.getNimbase().get("B");

        Molecule toUpdate = new Molecule("test add",
                atomA.copy("Updated", 2),
                atomB.copy("Updated", 2));

        server.startClientSessions();
        clientA.start();
        clientB.start();

        clientA.proposeUpdate(toUpdate);

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (!server.getNimbase().get("A").getStringData().equals("Updated")
                && !clientA.getNimbase().get("A").getStringData().equals("Updated")
                && !clientB.getNimbase().get("A").getStringData().equals("Updated")
                && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }

        compareAllNimbases();

        // allow threads to finish
        server.stopClientSessions();
        clientA.stop();
        clientB.stop();
        // TODO: assert everyone stopped
    }
}