package org.tripledip.dipcloud;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NimbaseTest {

    private Nimbase numbus;

    @Before
    public void setUp() throws Exception {
        numbus = new Nimbase();
        Atom atom = new Atom("A", 42, "A", 42, 42.0);
        numbus.add(atom);
    }

    @Test
    public void testAddNew() throws Exception {
        Atom atom = new Atom("B", 43, "B", 43, 43.0);
        assertTrue(numbus.add(atom));
        assertEquals(2, numbus.size());

        Atom retrieved = numbus.get(atom.getId());
        assertEquals(atom.getDoubleData(), retrieved.getDoubleData(), 0.0d);
    }

    @Test
    public void testAddExisting() throws Exception {
        Atom atom = numbus.get("A");
        assertFalse(numbus.add(atom));
        assertEquals(1, numbus.size());
    }

    @Test
    public void testAddLikeUpdate() throws Exception {
        Atom atom = numbus.get("A");
        assertTrue(numbus.add(atom.copy(5d, 100)));
        assertEquals(1, numbus.size());

        Atom retrieved = numbus.get(atom.getId());
        assertEquals(5d, retrieved.getDoubleData(), 0.0d);
    }

    @Test
    public void testUpdateNewer() throws Exception {
        Atom atom = numbus.get("A");
        assertTrue(numbus.update(atom.copy("pizza", 100)));
        assertEquals(1, numbus.size());

        Atom retrieved = numbus.get(atom.getId());
        assertEquals("pizza", retrieved.getStringData());
    }

    @Test
    public void testUpdateOlder() throws Exception {
        Atom atom = numbus.get("A");
        assertFalse(numbus.update(atom.copy(0, 0)));
        assertEquals(1, numbus.size());

        Atom retrieved = numbus.get(atom.getId());
        assertEquals(42, retrieved.getIntData());
    }

    @Test
    public void testUpdateNonexistent() throws Exception {
        Atom atom = new Atom("C", 44, "C", 44, 44.0);
        assertFalse(numbus.update(atom));
        assertEquals(1, numbus.size());
    }

    @Test
    public void testRemoveExisting() throws Exception {
        Atom atom = numbus.get("A");
        assertTrue(numbus.remove(atom));
        assertEquals(1, numbus.size());
    }

    @Test
    public void testRemoveNonexistent() throws Exception {
        Atom atom = new Atom("D", 45, "D", 45, 45.0);
        assertFalse(numbus.remove(atom));
        assertEquals(1, numbus.size());
    }
}