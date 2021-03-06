package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.contract.InBoxListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InBoxTest {

    private static final long MAX_WAIT_NANOS = (long) 2e9;
    private static final long SLEEP_MILLIS = 10;

    private TestConnector connector;
    private TestListener listener;
    private InBox<String> inBox;
    private List<String> testItems = Arrays.asList("one", "two", "three", "four", "five", "six");

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector(testItems.iterator());
        listener = new TestListener();
        inBox = new InBox<>(connector, listener);
    }

    @Test
    public void testProcess() throws Exception {
        // manually send all test items to our listener
        for (String s : testItems) {
            inBox.process(s);
        }

        assertEquals(testItems.size(), listener.getReceived().size());
        assertArrayEquals(testItems.toArray(), listener.getReceived().toArray());
    }

    @Test
    public void testProcessNext() throws Exception {
        // synchronously drain the connector, sending test items to our listener
        while (listener.getReceived().size() < testItems.size()) {
            inBox.processNext();
        }

        assertEquals(testItems.size(), listener.getReceived().size());
        assertArrayEquals(testItems.toArray(), listener.getReceived().toArray());
    }

    @Test
    public void testGetRunnable() throws Exception {
        // fire up a background thread for the inBox
        Thread inBoxThread = new Thread(inBox.getRunnable());
        inBoxThread.start();

        // sleep and poll until all test items arrive at our listener
        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (testItems.size() > listener.getReceived().size() && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }

        // interrupt will allow the inBox runnable to return
        inBoxThread.interrupt();
        Thread.sleep(SLEEP_MILLIS);
        assertFalse(inBoxThread.isAlive());

        assertEquals(testItems.size(), listener.getReceived().size());
        assertArrayEquals(testItems.toArray(), listener.getReceived().toArray());
    }

    private static class TestConnector implements Connector<String> {
        public Iterator<String> toRead;

        public TestConnector(Iterator<String> toRead) {
            this.toRead = toRead;
        }

        @Override
        public String readNext() throws InterruptedException {
            if (toRead.hasNext()) {
                return toRead.next();
            }
            Thread.sleep(MAX_WAIT_NANOS);
            return null;
        }

        @Override
        public void write(String outData) {
        }
    }

    private static class TestListener implements InBoxListener<String> {
        private final List<String> received = new ArrayList<>();

        @Override
        public synchronized void onInboxItemArrived(String item) {
            received.add(item);
        }

        public synchronized List<String> getReceived() {
            return received;
        }
    }
}
