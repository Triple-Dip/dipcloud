package org.tripledip.dipcloud.local.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AtomizerTest {

    private Crudable<Atom> numturfus;
    private Atomizer rotomozer;

    @Before
    public void setUp() throws Exception {
        numturfus = new Nimbase();
        rotomozer = new Atomizer(numturfus);

        Atom atom = new Atom("A", 42, "A", 42, 42.0d);
        numturfus.add(atom);
    }

    @Test
    public void testAddPartNew() throws Exception {
        Atom atomA = numturfus.get("A");
        Atom atomB = new Atom("B", 43, "B", 43, 43.0d);

        Molecule molecule = new Molecule("channel", atomA, atomB);
        Set<Atom> changed = rotomozer.add(molecule);

        assertEquals(1, changed.size());
        assertEquals(atomB, changed.toArray()[0]);
    }

    @Test
    public void testAddPartUpdated() throws Exception {
        Atom atomA = numturfus.get("A");
        Atom atomB = new Atom("B", 43, "B", 43, 43.0d);
        numturfus.add(atomB);

        atomB = atomB.copy("pizza", 100);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        Set<Atom> changed = rotomozer.add(molecule);

        assertEquals(1, changed.size());
        assertEquals(atomB, changed.toArray()[0]);
    }

    @Test
    public void testUpdatePartNew() throws Exception {
        Atom atomA = numturfus.get("A");
        Atom atomB = new Atom("B", 43, "B", 43, 43.0d);
        numturfus.add(atomB);

        atomA = atomA.copy("pizza", 100);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        Set<Atom> changed = rotomozer.update(molecule);

        assertEquals(1, changed.size());
        assertEquals(atomA, changed.toArray()[0]);
    }

    @Test
    public void testRemove() throws Exception {
        Atom atomA = numturfus.get("A");
        Atom atomB = new Atom("B", 43, "B", 43, 43.0d);
        Atom atomC = new Atom("C", 44, "C", 44, 44.0d);
        numturfus.add(atomB);
        numturfus.add(atomC);

        Molecule molecule = new Molecule("channel", atomA, atomB);
        Set<Atom> changed = rotomozer.remove(molecule);

        assertEquals(2, changed.size());
        assertTrue(changed.contains(atomA));
        assertTrue(changed.contains(atomB));
        assertFalse(changed.contains(atomC));
    }
}
