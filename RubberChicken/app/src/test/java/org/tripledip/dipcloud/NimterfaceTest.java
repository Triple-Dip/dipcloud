package org.tripledip.dipcloud;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class NimterfaceTest {

    private Nimbase numbus;
    private Nimterface numturfus;

    @Before
    public void setUp() throws Exception {
        this.numbus = new Nimbase();

        Atom atom = new Atom("A", 42, "A", 42, 42d);
        numbus.add(atom);

        this.numturfus = new Nimterface(numbus);
    }

    @Test
    public void testNotifyAtomAdded() throws Exception {
        TestListener<Atom> atomListener = new TestListener<>();
        numturfus.registerIdListener("B", atomListener);

        Atom atom = new Atom("B", 43, "B", 43, 43d);
        numturfus.add(new Molecule("testChannel", atom));

        assertNotNull(numturfus.get(atom.getId()));
        assertNotNull(atomListener.lastAdded);
        assertEquals(atom.getId(), atomListener.lastAdded.getId());
        assertEquals(atom.getIntData(), atomListener.lastAdded.getIntData());
    }

    private static class TestListener<T> implements NimterfaceListener<T> {
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