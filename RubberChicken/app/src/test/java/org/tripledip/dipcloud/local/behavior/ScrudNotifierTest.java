package org.tripledip.dipcloud.local.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.contract.ScrudListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ScrudNotifierTest {

    private ScrudNotifier<String> notifier;
    private TestListener<String> listenerA;
    private TestListener<String> listenerB;

    @Before
    public void setUp() throws Exception {
        this.notifier = new ScrudNotifier();
        listenerA = new TestListener<String>();
        listenerB = new TestListener<String>();
    }

    @Test
    public void testNotifyCorrectSubject() throws Exception {
        notifier.registerListener("Subject A", listenerA);
        notifier.registerListener("Subject B", listenerB);

        notifier.notifyAdded("Subject A", "added");
        assertNull(listenerB.lastAdded);
        assertEquals("added", listenerA.lastAdded);

        notifier.notifyUpdated("Subject A", "updated");
        assertNull(listenerB.lastUpdated);
        assertEquals("updated", listenerA.lastUpdated);

        notifier.notifyRemoved("Subject A", "removed");
        assertNull(listenerB.lastRemoved);
        assertEquals("removed", listenerA.lastRemoved);

        notifier.notifySent("Subject A", "sent");
        assertNull(listenerB.lastSent);
        assertEquals("sent", listenerA.lastSent);
    }

    private static class TestListener<T> implements ScrudListener<T> {
        public T lastAdded = null;
        public T lastUpdated = null;
        public T lastRemoved = null;
        public T lastSent = null;

        @Override
        public void onAdded(T thing) {
            lastAdded = thing;
        }

        @Override
        public void onUpdated(T thing) {
            lastUpdated = thing;
        }

        @Override
        public void onRemoved(T thing) {
            lastRemoved = thing;
        }

        @Override
        public void onSent(T thing) {
            lastSent = thing;
        }
    }
}
