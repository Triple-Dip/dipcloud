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
import static org.junit.Assert.assertTrue;

public class InBoxTest {

    private static final long MAX_WAIT_NANOS = (long) 1e9;
    private static final long SLEEP_MILLIS = 10;

    private TestConnector connector;
    private TestListener listener;
    private InBox<String> inBox;
    private List<String> testItems = Arrays.asList("one", "two", "three", "four", "five", "six");

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
        public void write(String outData) { }
    }

    private static class TestListener implements InBoxListener<String> {
        public List<String> received;

        public TestListener() {
            received = new ArrayList<>();
        }

        @Override
        public void onInboxItemArrived(String item) {
            received.add(item);
        }
    }

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

        assertEquals(testItems.size(), listener.received.size());
        assertArrayEquals(testItems.toArray(), listener.received.toArray());
    }

    @Test
    public void testProcessNext() throws Exception {
        // synchronously drain the connector, sending test items to our listener
        while (listener.received.size() < testItems.size()) {
            inBox.processNext();
        }

        assertEquals(testItems.size(), listener.received.size());
        assertArrayEquals(testItems.toArray(), listener.received.toArray());
    }

    @Test
    public void testGetRunnable() throws Exception {
        // fire up a background thread for the inBox
        Thread inBoxThread = new Thread(inBox.getRunnable());
        inBoxThread.start();

        // sleep and poll until all test items arrive at our listener
        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (testItems.size() > listener.received.size() && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }
        assertEquals(testItems.size(), listener.received.size());
        assertArrayEquals(testItems.toArray(), listener.received.toArray());

        // interrupt will allow the inBox runnable to return
        inBoxThread.interrupt();
        Thread.sleep(SLEEP_MILLIS);
        assertEquals(Thread.State.TERMINATED, inBoxThread.getState());
    }
}
