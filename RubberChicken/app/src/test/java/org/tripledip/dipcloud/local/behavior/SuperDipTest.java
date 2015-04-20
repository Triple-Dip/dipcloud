package org.tripledip.dipcloud.local.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.Comparator;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class SuperDipTest {

    private Crudable<Atom> numbus;
    private DipAccess dupCluud;
    private TestListener<Atom> idListener;
    private TestListener<Molecule> channelListener;
    private TestListener<Smashable> smashableListener;
    private TestListener<Atom> wrongIdListener = new TestListener<>();
    private TestListener<Molecule> wrongChannelListener = new TestListener<>();
    private TestListener<Smashable> wrongSmashableListener = new TestListener<>();


    @Before
    public void setUp() throws Exception {
        numbus = new Nimbase();
        numbus.add(makeAtom("A", 42));
        dupCluud = new SuperDip(numbus);
        idListener = new TestListener<>();
        channelListener = new TestListener<>();
        smashableListener = new TestListener<>();
        dupCluud.registerSmashable(new TestSmashable());
    }

    private Atom makeAtom(String name, int number) {
        return new Atom(name, number, name, number, number);
    }

    @Test
    public void testAddNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("B", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getId(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", smashableListener);

        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", numbus.get("A"), atomB);
        dupCluud.proposeAdd(molecule);

        Smashable smashable = new TestSmashable("butts",4,2.0);
        dupCluud.proposeAdd(smashable);

        assertEquals(molecule.getAction(), Molecule.Action.ADD);
        assertNull(wrongIdListener.lastAdded);
        assertNull(wrongChannelListener.lastAdded);
        assertNull(wrongSmashableListener.lastAdded);
        assertEquals(atomB, idListener.lastAdded);
        assertEquals(molecule, channelListener.lastAdded);
        assertEquals(smashable, smashableListener.lastAdded);
    }

    @Test
    public void testUpdateNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("A", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getId(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        Atom atomA = numbus.get("A").copy("pizza", 100);
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.proposeUpdate(molecule);

        Smashable intialSmashable = new TestSmashable("butts",4,2.0);
        dupCluud.proposeAdd(intialSmashable);

        Smashable updateSmashable = new TestSmashable("farts",5,2.3);
        dupCluud.proposeUpdate(updateSmashable);

        assertEquals(molecule.getAction(), Molecule.Action.UPDATE);
        assertNull(wrongIdListener.lastUpdated);
        assertNull(wrongChannelListener.lastUpdated);
        assertNull(wrongSmashableListener.lastUpdated);
        assertEquals(atomA, idListener.lastUpdated);
        assertEquals(molecule, channelListener.lastUpdated);
        assertEquals(updateSmashable, smashableListener.lastUpdated);
    }

    @Test
    public void testRemoveNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("A", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getId(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        Atom atomA = numbus.get("A");
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.proposeRemove(molecule);

        Smashable intialSmashable = new TestSmashable("butts",4,2.0);
        dupCluud.proposeAdd(intialSmashable);

        dupCluud.proposeRemove(intialSmashable);

        assertEquals(molecule.getAction(), Molecule.Action.REMOVE);
        assertNull(wrongIdListener.lastRemoved);
        assertNull(wrongChannelListener.lastRemoved);
        assertNull(wrongSmashableListener.lastRemoved);
        assertEquals(atomA, idListener.lastRemoved);
        assertEquals(molecule, channelListener.lastRemoved);
        assertEquals(intialSmashable, smashableListener.lastRemoved);

    }

    @Test
    public void testSendNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("B", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getId(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        Atom atomA = numbus.get("A");
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.proposeSend(molecule);

        Smashable sentSmashable = new TestSmashable("butts",4,2.0);
        dupCluud.proposeSend(sentSmashable);

        assertEquals(molecule.getAction(), Molecule.Action.SEND);
        assertNull(wrongIdListener.lastSent);
        assertNull(wrongChannelListener.lastSent);
        assertNull(wrongSmashableListener.lastSent);
        assertEquals(atomB, idListener.lastSent);
        assertEquals(molecule, channelListener.lastSent);
        assertEquals(sentSmashable, smashableListener.lastSent);
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

    private static class TestSmashable extends Smashable {

        public static final String A_STRING = "aString";
        public static final String AN_INT = "anInt";
        public static final String A_DOUBLE = "aDouble";

        private String aString;
        private int anInt;
        private double aDouble;

        public TestSmashable(){

        }

        public TestSmashable(String aString, int anInt, double aDouble) {
            this.aString = aString;
            this.anInt = anInt;
            this.aDouble = aDouble;
        }

        @Override
        public void smashMe(Molecule molecule, long sequenceNumber) {

            molecule.addOrReplace(new Atom(A_STRING,sequenceNumber, aString));
            molecule.addOrReplace(new Atom(AN_INT,sequenceNumber, anInt));
            molecule.addOrReplace(new Atom(A_DOUBLE,sequenceNumber, aDouble));

        }

        @Override
        public void unsmashMe(Molecule molecule) {

            aString = molecule.findById(A_STRING).getStringData();
            anInt = molecule.findById(AN_INT).getIntData();
            aDouble = molecule.findById(A_DOUBLE).getDoubleData();

        }

        @Override
        public Smashable newInstance() {
            return new TestSmashable();
        }

        public String getaString() {
            return aString;
        }

        public int getAnInt() {
            return anInt;
        }

        public double getaDouble() {
            return aDouble;
        }

        // For testing
        @Override
        public boolean equals(Object obj){

            if (obj == this)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (obj instanceof TestSmashable)
            {
                TestSmashable other = (TestSmashable)obj;
                return other.getaString().equals(getaString()) &&
                        other.getaDouble() == getaDouble() &&
                        other.getAnInt() == getAnInt();
            } else {
                return false;
            }

        }
    }

}
