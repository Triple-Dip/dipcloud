package org.tripledip.dipcloud.local.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class SuperDipTest {

    private Crudable<Atom> numbus;
    private SuperDip dupCluud;
    private TestListener<Atom> idListener;
    private TestListener<Molecule> channelListener;
    private TestListener<Atom> wrongIdListener = new TestListener<>();
    private TestListener<Molecule> wrongChannelListener = new TestListener<>();


    @Before
    public void setUp() throws Exception {
        numbus = new Nimbase();
        numbus.add(makeAtom("A", 42));
        dupCluud = new SuperDip(numbus);
        idListener = new TestListener<>();
        channelListener = new TestListener<>();
    }

    private Atom makeAtom(String name, int number) {
        return new Atom(name, number, name, number, number);
    }

    @Test
    public void testAddNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("B", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);

        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", numbus.get("A"), atomB);
        dupCluud.add(molecule);

        assertEquals(molecule.getAction(), Molecule.Action.ADD);
        assertNull(wrongIdListener.lastAdded);
        assertNull(wrongChannelListener.lastAdded);
        assertEquals(atomB, idListener.lastAdded);
        assertEquals(molecule, channelListener.lastAdded);
    }

    @Test
    public void testUpdateNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("A", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);

        Atom atomA = numbus.get("A").copy("pizza", 100);
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.update(molecule);

        assertEquals(molecule.getAction(), Molecule.Action.UPDATE);
        assertNull(wrongIdListener.lastUpdated);
        assertNull(wrongChannelListener.lastUpdated);
        assertEquals(atomA, idListener.lastUpdated);
        assertEquals(molecule, channelListener.lastUpdated);
    }

    @Test
    public void testRemoveNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("A", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);

        Atom atomA = numbus.get("A");
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.remove(molecule);

        assertEquals(molecule.getAction(), Molecule.Action.REMOVE);
        assertNull(wrongIdListener.lastRemoved);
        assertNull(wrongChannelListener.lastRemoved);
        assertEquals(atomA, idListener.lastRemoved);
        assertEquals(molecule, channelListener.lastRemoved);
    }

    @Test
    public void testSendNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("B", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);

        Atom atomA = numbus.get("A");
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.send(molecule);

        assertEquals(molecule.getAction(), Molecule.Action.SEND);
        assertNull(wrongIdListener.lastSent);
        assertNull(wrongChannelListener.lastSent);
        assertEquals(atomB, idListener.lastSent);
        assertEquals(molecule, channelListener.lastSent);
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
