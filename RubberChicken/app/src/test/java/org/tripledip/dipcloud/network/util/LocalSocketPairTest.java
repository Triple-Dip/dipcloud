package org.tripledip.dipcloud.network.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        assertFalse(localSocketPair.getClientSide().isClosed());
        assertFalse(localSocketPair.getServerSide().isClosed());
    }

    @After
    public void tearDown() throws Exception {
        localSocketPair.close();
    }

    private int sendStreamData(OutputStream from, InputStream to, byte[] message) {

        try {
            from.write(message);
        } catch (IOException e) {
            return -1;
        }

        int bytesRead = 0;
        byte[] inMessage = new byte[message.length];
        try {
            bytesRead = to.read(inMessage);
        } catch (IOException e) {
            return bytesRead;
        }

        return bytesRead;
    }

    @Test
    public void testStreamClientToServer() throws Exception {
        String message = "Client says current millis are " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendStreamData(
                localSocketPair.getClientSide().getOutputStream(),
                localSocketPair.getServerSide().getInputStream(),
                messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

    @Test
    public void testStreamServerToClient() throws Exception {
        String message = "Current millis according to server: " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendStreamData(
                localSocketPair.getServerSide().getOutputStream(),
                localSocketPair.getClientSide().getInputStream(),
                messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

}
