package org.tripledip.dipcloud.network.behavior;

import org.junit.After;
import org.junit.Before;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.network.util.LocalSocketPair;
import org.tripledip.dipcloud.network.util.SocketProtocConnector;

import static org.junit.Assert.assertFalse;


public class SocketProtocDipTripleTest extends DipTripleTest {

    private static final int PORT_A = 55555;
    private static final int PORT_B = 55556;

    private LocalSocketPair aToServer;
    private LocalSocketPair bToServer;

    // Set up a server and two clients with socket protocol buffer connectors.
    @Before
    public void setUp() throws Exception {
        server = new DipServer(new Nimbase());

        aToServer = new LocalSocketPair();
        aToServer.open(PORT_A);
        assertFalse(aToServer.getClientSide().isClosed());
        assertFalse(aToServer.getServerSide().isClosed());

        clientA = new DipClient(new Nimbase(), new SocketProtocConnector(aToServer.getClientSide()));
        server.addSession(new SocketProtocConnector(aToServer.getServerSide()));

        bToServer = new LocalSocketPair();
        bToServer.open(PORT_B);
        assertFalse(bToServer.getClientSide().isClosed());
        assertFalse(bToServer.getServerSide().isClosed());

        clientB = new DipClient(new Nimbase(), new SocketProtocConnector(bToServer.getClientSide()));
        server.addSession(new SocketProtocConnector(bToServer.getServerSide()));
    }

    @After
    public void tearDown() throws Exception {
        aToServer.close();
        bToServer.close();
    }
}
