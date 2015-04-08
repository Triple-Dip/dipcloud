package org.tripledip.dipcloud.network.behavior;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.InBoxListener;
import org.tripledip.dipcloud.network.util.LocalSocketPair;
import org.tripledip.dipcloud.network.util.SocketProtocConnector;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


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
        assertTrue(aToServer.getClientSide().isClosed());
        assertTrue(aToServer.getServerSide().isClosed());

        bToServer.close();
        assertTrue(bToServer.getClientSide().isClosed());
        assertTrue(bToServer.getServerSide().isClosed());
    }

    @Test
    public void testBoxesSendReceive() throws Exception {
        Molecule moleculeOut = new Molecule("test", Molecule.Action.SEND, new ArrayList<Atom>());

        Socket clientSide = aToServer.getClientSide();
        assertTrue(clientSide.isConnected());
        assertFalse(clientSide.isClosed());

        SocketProtocConnector clientConnector = new SocketProtocConnector(clientSide);

        Socket serverSide = aToServer.getServerSide();
        assertTrue(serverSide.isConnected());
        assertFalse(serverSide.isClosed());

        SocketProtocConnector serverConnector = new SocketProtocConnector(serverSide);

        final List<Molecule> moleculesIn = new ArrayList<>();

        OutBox<Molecule> outBox = new OutBox<>(clientConnector);
        InBox<Molecule> inBox = new InBox<>(serverConnector, new InBoxListener<Molecule>() {
            @Override
            public void onInboxItemArrived(Molecule item) {
                moleculesIn.add(item);
            }
        });

        Thread outThread = new Thread(outBox.getRunnable());
        Thread inThread = new Thread(inBox.getRunnable());

        outBox.add(moleculeOut);

        inThread.start();
        outThread.start();

        outBox.add(moleculeOut);
        outBox.add(moleculeOut);

        Thread.sleep(1000);

        inThread.interrupt();
        outThread.interrupt();

        assertEquals(3, moleculesIn.size());
        Molecule moleculeIn = moleculesIn.get(0);

        assertEquals(moleculeOut.getChannel(), moleculeIn.getChannel());
        assertEquals(moleculeOut.getAction(), moleculeIn.getAction());
    }

    @Test
    public void testSessionsSendReceive() throws Exception {
        Molecule moleculeOut = new Molecule("test", Molecule.Action.SEND, new ArrayList<Atom>());

        Socket clientSide = aToServer.getClientSide();
        assertTrue(clientSide.isConnected());
        assertFalse(clientSide.isClosed());

        SocketProtocConnector clientConnector = new SocketProtocConnector(clientSide);

        Socket serverSide = aToServer.getServerSide();
        assertTrue(serverSide.isConnected());
        assertFalse(serverSide.isClosed());

        SocketProtocConnector serverConnector = new SocketProtocConnector(serverSide);

        final List<Molecule> moleculesIn = new ArrayList<>();

        Session<Molecule> clientSession = new Session<>(clientConnector, new InBoxListener<Molecule>() {
            @Override
            public void onInboxItemArrived(Molecule item) {}
        });

        Session<Molecule> serverSession = new Session<>(serverConnector, new InBoxListener<Molecule>() {
            @Override
            public void onInboxItemArrived(Molecule item) {
                moleculesIn.add(item);
            }
        });

        // TODO: why is client inbox breaking the test?
        serverSession.startInBox();
        serverSession.startOutBox();
        //clientSession.startInBox();
        clientSession.startOutBox();

        clientSession.sendMessage(moleculeOut);
        clientSession.sendMessage(moleculeOut);
        clientSession.sendMessage(moleculeOut);

        Thread.sleep(1000);

        serverSession.stopInBox();
        serverSession.stopOutBox();
        clientSession.stopInBox();
        clientSession.stopOutBox();

        assertEquals(3, moleculesIn.size());
        Molecule moleculeIn = moleculesIn.get(0);

        assertEquals(moleculeOut.getChannel(), moleculeIn.getChannel());
        assertEquals(moleculeOut.getAction(), moleculeIn.getAction());
    }
}