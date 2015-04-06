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
        assertTrue(aToServer.getClientSide().isOpen());
        assertTrue(aToServer.getServerSide().isOpen());

        clientA = new DipClient(new Nimbase(), new SocketProtocConnector(aToServer.getClientSide()));
        server.addSession(new SocketProtocConnector(aToServer.getServerSide()));

        bToServer = new LocalSocketPair();
        bToServer.open(PORT_B);
        assertTrue(bToServer.getClientSide().isOpen());
        assertTrue(bToServer.getServerSide().isOpen());

        clientB = new DipClient(new Nimbase(), new SocketProtocConnector(bToServer.getClientSide()));
        server.addSession(new SocketProtocConnector(bToServer.getServerSide()));
    }

    @After
    public void tearDown() throws Exception {
        aToServer.close();
        assertFalse(aToServer.getClientSide().isOpen());
        assertFalse(aToServer.getServerSide().isOpen());

        bToServer.close();
        assertFalse(bToServer.getClientSide().isOpen());
        assertFalse(bToServer.getServerSide().isOpen());
    }

    @Test
    public void testSimpleSendReceive() throws Exception {
        Molecule moleculeOut = new Molecule("test", Molecule.Action.SEND, new ArrayList<Atom>());

        SocketChannel clientSide = aToServer.getClientSide();
        assertTrue(clientSide.isConnected());
        assertTrue(clientSide.isOpen());

        Socket clientSocket = clientSide.socket();
        assertTrue(clientSocket.isConnected());
        assertFalse(clientSocket.isClosed());

        SocketProtocConnector clientConnector = new SocketProtocConnector(clientSide);
        clientConnector.write(moleculeOut);

        SocketChannel serverSide = aToServer.getServerSide();
        assertTrue(serverSide.isConnected());
        assertTrue(serverSide.isOpen());

        Socket serverSocket = serverSide.socket();
        assertTrue(serverSocket.isConnected());
        assertFalse(serverSocket.isClosed());

        SocketProtocConnector serverConnector = new SocketProtocConnector(serverSide);
        Molecule moleculeIn = serverConnector.readNext();

        assertEquals(moleculeOut.getChannel(), moleculeIn.getChannel());
        assertEquals(moleculeOut.getAction(), moleculeIn.getAction());
    }

    @Test
    public void testAsyncSendReceive() throws Exception {
        final Molecule moleculeOut = new Molecule("test", Molecule.Action.SEND, new ArrayList<Atom>());

        SocketChannel clientSide = aToServer.getClientSide();
        assertTrue(clientSide.isConnected());
        assertTrue(clientSide.isOpen());

        Socket clientSocket = clientSide.socket();
        assertTrue(clientSocket.isConnected());
        assertFalse(clientSocket.isClosed());

        final SocketProtocConnector clientConnector = new SocketProtocConnector(clientSide);

        SocketChannel serverSide = aToServer.getServerSide();
        assertTrue(serverSide.isConnected());
        assertTrue(serverSide.isOpen());

        Socket serverSocket = serverSide.socket();
        assertTrue(serverSocket.isConnected());
        assertFalse(serverSocket.isClosed());

        final SocketProtocConnector serverConnector = new SocketProtocConnector(serverSide);

        final List<Molecule> moleculesIn = new ArrayList<>();
        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Molecule moleculeIn = serverConnector.readNext();
                        moleculesIn.add(moleculeIn);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {
                clientConnector.write(moleculeOut);
                clientConnector.write(moleculeOut);
                clientConnector.write(moleculeOut);
            }
        });

        reader.start();
        writer.start();
        Thread.sleep(1000);
        reader.interrupt();

        assertEquals(3, moleculesIn.size());
        Molecule moleculeIn = moleculesIn.get(0);
        assertEquals(moleculeOut.getChannel(), moleculeIn.getChannel());
        assertEquals(moleculeOut.getAction(), moleculeIn.getAction());
    }

    @Test
    public void testBoxesSendReceive() throws Exception {
        Molecule moleculeOut = new Molecule("test", Molecule.Action.SEND, new ArrayList<Atom>());

        SocketChannel clientSide = aToServer.getClientSide();
        assertTrue(clientSide.isConnected());
        assertTrue(clientSide.isOpen());

        Socket clientSocket = clientSide.socket();
        assertTrue(clientSocket.isConnected());
        assertFalse(clientSocket.isClosed());

        SocketProtocConnector clientConnector = new SocketProtocConnector(clientSide);

        SocketChannel serverSide = aToServer.getServerSide();
        assertTrue(serverSide.isConnected());
        assertTrue(serverSide.isOpen());

        Socket serverSocket = serverSide.socket();
        assertTrue(serverSocket.isConnected());
        assertFalse(serverSocket.isClosed());

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

        SocketChannel clientSide = aToServer.getClientSide();
        assertTrue(clientSide.isConnected());
        assertTrue(clientSide.isOpen());

        Socket clientSocket = clientSide.socket();
        assertTrue(clientSocket.isConnected());
        assertFalse(clientSocket.isClosed());

        SocketProtocConnector clientConnector = new SocketProtocConnector(clientSide);

        SocketChannel serverSide = aToServer.getServerSide();
        assertTrue(serverSide.isConnected());
        assertTrue(serverSide.isOpen());

        Socket serverSocket = serverSide.socket();
        assertTrue(serverSocket.isConnected());
        assertFalse(serverSocket.isClosed());

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