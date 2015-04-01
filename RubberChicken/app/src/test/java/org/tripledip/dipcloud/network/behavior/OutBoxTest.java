package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.network.contract.Connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



public class OutBoxTest {
    private static final long MAX_WAIT_NANOS = (long) 2e9;
    private static final long SLEEP_MILLIS = 10;

    private TestConnector connector;
    private OutBox<String> outBox;
    private List<String> testItems = Arrays.asList("one", "two", "three", "four", "five", "six");

    private static class TestConnector implements Connector<String> {
        private final List<String> sent = new ArrayList<>();

        @Override
        public synchronized String readNext() throws InterruptedException {
            return null;
        }

        @Override
        public synchronized void write(String outData) {
            sent.add(outData);
        }

        public synchronized List<String> getSent() {
            return sent;
        }
    }

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        outBox = new OutBox<>(connector);
    }

    @Test
    public void testSendAll() throws Exception {
        // addOrReplace all the test messages to the outBox
        for (String s : testItems) {
            outBox.add(s);
        }
        assertTrue(connector.getSent().isEmpty());

        // synchronously move all test messages to our connector
        outBox.sendAll();
        assertEquals(testItems.size(), connector.getSent().size());
        assertArrayEquals(testItems.toArray(), connector.getSent().toArray());
    }

    @Test
    public void testGetRunnable() throws Exception {
        // fire up a background thread for the outBox
        Thread outBoxThread = new Thread(outBox.getRunnable());
        outBoxThread.start();

        // addOrReplace all the test messages to the outBox
        for (String s : testItems) {
            outBox.add(s);
            Thread.sleep(SLEEP_MILLIS);
        }

        // sleep and poll until all test messages arrive at our connector
        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (testItems.size() > connector.getSent().size() && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // interrupt will allow the outBox runnable to return
        outBoxThread.interrupt();
        Thread.sleep(SLEEP_MILLIS);
        assertFalse(outBoxThread.isAlive());

        assertEquals(testItems.size(), connector.getSent().size());
        assertArrayEquals(testItems.toArray(), connector.getSent().toArray());
    }
}
