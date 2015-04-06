package org.tripledip.dipcloud.network.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        assertTrue(localSocketPair.getClientSide().socket().isConnected());
        assertTrue(localSocketPair.getServerSide().socket().isConnected());
    }

    @After
    public void tearDown() throws Exception {
        localSocketPair.close();
    }

    private int sendChannelData(SocketChannel from, SocketChannel to, byte[] message) {
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
    public void testChannelClientToServer() throws Exception {
        String message = "Client says current millis are " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendChannelData(
                localSocketPair.getClientSide(),
                localSocketPair.getServerSide(),
                messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

    @Test
    public void testChannelServerToClient() throws Exception {
        String message = "Current millis according to server: " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendChannelData(
                localSocketPair.getServerSide(),
                localSocketPair.getClientSide(),
                messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

    @Test
    public void testStreamClientToServer() throws Exception {
        String message = "Client says current millis are " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendStreamData(
                localSocketPair.getClientSide().socket().getOutputStream(),
                localSocketPair.getServerSide().socket().getInputStream(),
                messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

    @Test
    public void testSocketServerToClient() throws Exception {
        String message = "Current millis according to server: " + System.currentTimeMillis();
        byte[] messageBytes = message.getBytes();
        int bytesSent = sendStreamData(
                localSocketPair.getServerSide().socket().getOutputStream(),
                localSocketPair.getClientSide().socket().getInputStream(),
                messageBytes);
        assertEquals(messageBytes.length, bytesSent);
    }

}
