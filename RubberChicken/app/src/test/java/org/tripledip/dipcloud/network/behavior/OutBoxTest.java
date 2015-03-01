package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.contract.Connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



public class OutBoxTest {
    private static final long MAX_WAIT_NANOS = (long) 1e9;
    private static final long SLEEP_MILLIS = 10;

    private TestConnector connector;
    private OutBox<String> outBox;
    private List<String> testItems = Arrays.asList("one", "two", "three", "four", "five", "six");

    private static class TestConnector implements Connector<String> {
        public List<String> sent;

        public TestConnector() {
            sent = new ArrayList<>();
        }

        @Override
        public String readNext() {
            return null;
        }

        @Override
        public void write(String outData) {
            sent.add(outData);
        }
    }

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        outBox = new OutBox<>(connector);
    }

    @Test
    public void testSendAll() throws Exception {
        // add all the test messages to the outBox
        for (String s : testItems) {
            outBox.add(s);
        }
        assertTrue(connector.sent.isEmpty());

        // synchronously move all test messages to our connector
        outBox.sendAll();
        assertEquals(testItems.size(), connector.sent.size());
        assertArrayEquals(testItems.toArray(), connector.sent.toArray());
    }

    @Test
    public void testGetRunnable() throws Exception {
        // fire up a background thread for the outBox
        Thread outBoxThread = new Thread(outBox.getRunnable());
        outBoxThread.start();

        // add all the test messages to the outBox
        for (String s : testItems) {
            outBox.add(s);
        }

        // sleep and poll until all test messages arrive at our connector
        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (testItems.size() > connector.sent.size() && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }
        assertEquals(testItems.size(), connector.sent.size());
        assertArrayEquals(testItems.toArray(), connector.sent.toArray());

        // interrupt will allow the outBox runnable to return
        outBoxThread.interrupt();
        Thread.sleep(SLEEP_MILLIS);
        assertEquals(Thread.State.TERMINATED, outBoxThread.getState());
    }
}
