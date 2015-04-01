package org.tripledip.dipcloud.network.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class LocalSocketPairTest {

    private static final int TEST_PORT = 55555;

    private LocalSocketPair localSocketPair;

    @Before
    public void setUp() throws Exception {
        localSocketPair = new LocalSocketPair();
        localSocketPair.open(TEST_PORT);

        assertNotNull(localSocketPair.getClientSide());
        assertNotNull(localSocketPair.getServerSide());

        assertTrue(localSocketPair.getClientSide().isConnected());
        assertTrue(localSocketPair.getServerSide().isConnected());
    }

    @After
    public void tearDown() throws Exception {
        localSocketPair.close();
    }

    private int sendData(SocketChannel from, SocketChannel to, byte[] message) {
        ByteBuffer buffer = ByteBuffer.wrap(message);

        // buffer clear == flip because limit == capacity
        buffer.clear();
        while (buffer.hasRemaining()) {
            try {
                from.write(buffer);
            } catch (IOException e) {
                // negative return indicates bytes written out
                return 0 - buffer.position();
            }
        }

        buffer.clear();
        while (buffer.hasRemaining()) {
            try {
                to.read(buffer);
            } catch (IOException e) {
                return buffer.position();
            }
        }

        return buffer.position();
    }

    @Test
    public void testClientToServer() throws Exception {
        String message = "Client says current millis are " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendData(localSocketPair.getClientSide(), localSocketPair.getServerSide(), messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

    @Test
    public void testServerToClient() throws Exception {
        String message = "Current millis according to server: " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendData(localSocketPair.getServerSide(), localSocketPair.getClientSide(), messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }
}
