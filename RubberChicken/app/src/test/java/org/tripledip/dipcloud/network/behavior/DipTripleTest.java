package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.util.InMemoryConnectorPair;

import static org.junit.Assert.assertEquals;


public class DipTripleTest {

    private static final long MAX_WAIT_NANOS = (long) 1e9;
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
        assertEquals(server.getNimbase().size(), toAdd.size());
        assertEquals(clientA.getNimbase().size(), toAdd.size());
        assertEquals(clientB.getNimbase().size(), toAdd.size());

        // allow threads to finish
        server.startClientSessions();
        clientA.stop();
        clientB.stop();
    }
}