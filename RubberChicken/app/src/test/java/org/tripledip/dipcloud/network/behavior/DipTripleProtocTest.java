package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.util.InMemoryConnectorPair;
import org.tripledip.dipcloud.network.util.InMemoryProtocConnectorPair;


public class DipTripleProtocTest extends DipTripleTest {

    // Set up a server and two clients with in-memory protocol buffer connectors.
    @Before
    public void setUp() throws Exception {
        server = new DipServer(new Nimbase());

        InMemoryConnectorPair<Molecule> aToServer = new InMemoryProtocConnectorPair();
        clientA = new DipClient(new Nimbase(), aToServer.getASendToB());
        server.addSession(aToServer.getBSendToA());

        InMemoryConnectorPair<Molecule> bToServer = new InMemoryProtocConnectorPair();
        clientB = new DipClient(new Nimbase(), bToServer.getASendToB());
        server.addSession(bToServer.getBSendToA());
    }
}
