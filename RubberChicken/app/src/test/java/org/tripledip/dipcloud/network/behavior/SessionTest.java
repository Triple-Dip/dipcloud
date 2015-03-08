package org.tripledip.dipcloud.network.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.network.contract.InBoxListener;
import org.tripledip.dipcloud.network.contract.util.InMemoryConnectorPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SessionTest {

    private static final long MAX_WAIT_NANOS = (long) 1e9;
    private static final long SLEEP_MILLIS = 10;

    private Session<String> sessionA;
    private Session<String> sessionB;
    private SessionListener<String> listenerA;
    private SessionListener<String> listenerB;

    private List<String> testItems = Arrays.asList("one", "two", "three", "four", "five", "six");

    @Before
    public void setUp() throws Exception {
        listenerA = new SessionListener<>();
        listenerB = new SessionListener<>();

        InMemoryConnectorPair<String> connectorPair = new InMemoryConnectorPair<>();

        sessionA = new Session<>(connectorPair.getASendToB(), listenerA);
        sessionB = new Session<>(connectorPair.getBSendToA(), listenerB);
    }

    @Test
    public void testASendToB() throws Exception {
        sendAllAndCheck(sessionA, sessionB, listenerB);
    }

    @Test
    public void testBSendToA() throws Exception {
        sendAllAndCheck(sessionB, sessionA, listenerA);
    }

    private void sendAllAndCheck(Session<String> sender, Session<String> receiver,
                                 SessionListener<String> listener) throws Exception {
        sender.startOutBox();
        receiver.startInBox();

        for (String string : testItems) {
            sender.sendMessage(string);
        }

        long endTime = System.nanoTime() + MAX_WAIT_NANOS;
        while (testItems.size() > listener.arrived.size() && System.nanoTime() < endTime) {
            Thread.sleep(SLEEP_MILLIS);
        }
        assertEquals(testItems.size(), listener.arrived.size());
        assertArrayEquals(testItems.toArray(), listener.arrived.toArray());

        // allow threads to finish
        sender.stopOutBox();
        receiver.stopInBox();
        Thread.sleep(SLEEP_MILLIS);
        assertTrue(sender.outBoxIsStopped());
        assertTrue(receiver.inBoxIsStopped());
    }

    private static class SessionListener<T> implements InBoxListener<T> {
        public List<T> arrived = new ArrayList<>();

        @Override
        public void onInboxItemArrived(T item) {
            arrived.add(item);
        }
    }
}