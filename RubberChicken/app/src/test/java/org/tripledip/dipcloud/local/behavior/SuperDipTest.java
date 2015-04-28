package org.tripledip.dipcloud.local.behavior;

import org.junit.Before;
import org.junit.Test;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

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
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getChannel(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", smashableListener);

        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", numbus.get("A"), atomB);
        dupCluud.proposeAdd(molecule);

        TestSmashable smashable = new TestSmashable("butts",4,2.0);

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
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getChannel(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        Atom atomA = numbus.get("A").copy("pizza", 100);
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.proposeUpdate(molecule);

        TestSmashable testSmashable = new TestSmashable("butts",4,2.0);
        dupCluud.proposeAdd(testSmashable);

        testSmashable.setaString("farts");
        testSmashable.setAnInt(5);
        testSmashable.setaDouble(2.3);

        dupCluud.proposeUpdate(testSmashable);

        assertEquals(molecule.getAction(), Molecule.Action.UPDATE);
        assertNull(wrongIdListener.lastUpdated);
        assertNull(wrongChannelListener.lastUpdated);
        assertNull(wrongSmashableListener.lastUpdated);
        assertEquals(atomA, idListener.lastUpdated);
        assertEquals(molecule, channelListener.lastUpdated);
        assertEquals(testSmashable, smashableListener.lastUpdated);
    }

    @Test
    public void testRemoveNotifyCorrectSubject() throws Exception {
        dupCluud.getIdListeners().registerListener("A", idListener);
        dupCluud.getChannelListeners().registerListener("channel", channelListener);
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getChannel(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        Atom atomA = numbus.get("A");
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.proposeRemove(molecule);

        TestSmashable intialSmashable = new TestSmashable("butts",4,2.0);
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
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getChannel(), smashableListener);

        dupCluud.getIdListeners().registerListener("wrong", wrongIdListener);
        dupCluud.getChannelListeners().registerListener("wrong", wrongChannelListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        Atom atomA = numbus.get("A");
        Atom atomB = makeAtom("B", 43);
        Molecule molecule = new Molecule("channel", atomA, atomB);
        dupCluud.proposeSend(molecule);

        TestSmashable sentSmashable = new TestSmashable("butts",4,2.0);
        dupCluud.proposeSend(sentSmashable);

        assertEquals(molecule.getAction(), Molecule.Action.SEND);
        assertNull(wrongIdListener.lastSent);
        assertNull(wrongChannelListener.lastSent);
        assertNull(wrongSmashableListener.lastSent);
        assertEquals(atomB, idListener.lastSent);
        assertEquals(molecule, channelListener.lastSent);
        assertEquals(sentSmashable, smashableListener.lastSent);
    }

    @Test
    public void testMultipleSmashableInstances() throws Exception {
        dupCluud.getSmashableListeners().registerListener(new TestSmashable().getChannel(), smashableListener);
        dupCluud.getSmashableListeners().registerListener("wrong", wrongSmashableListener);

        TestSmashable firstSmashable = new TestSmashable("butts",4,2.0);
        TestSmashable secondSmashable = new TestSmashable("farts",3,1.0);
        TestSmashable thirdSmashable = new TestSmashable("turds",6,9.0);

        dupCluud.proposeAdd(firstSmashable);
        assertEquals(firstSmashable, smashableListener.lastAdded);

        dupCluud.proposeAdd(secondSmashable);
        assertEquals(secondSmashable, smashableListener.lastAdded);

        dupCluud.proposeAdd(thirdSmashable);
        assertEquals(thirdSmashable, smashableListener.lastAdded);


        firstSmashable.setaString("butts2");
        dupCluud.proposeUpdate(firstSmashable);
        assertEquals(firstSmashable, smashableListener.lastUpdated);

        secondSmashable.setaString("farts2");
        dupCluud.proposeUpdate(secondSmashable);
        assertEquals(secondSmashable, smashableListener.lastUpdated);

        thirdSmashable.setaString("turds2");
        dupCluud.proposeUpdate(thirdSmashable);
        assertEquals(thirdSmashable, smashableListener.lastUpdated);


        // 1 test atom
        // 3 x Smashables with 3 fields = 9
        // 1 + 9 = 10 expected atoms in Nimbase
        assertEquals(10, numbus.size());
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
        public void smashMe(SmashableBuilder smashableBuilder) {
            smashableBuilder.addDoubleData(A_DOUBLE, aDouble);
            smashableBuilder.addIntData(AN_INT, anInt);
            smashableBuilder.addStringData(A_STRING, aString);
        }

        @Override
        public void unsmashMe(SmashableBuilder smashableBuilder) {
            this.aString = smashableBuilder.getStringData(A_STRING);
            this.anInt = smashableBuilder.getIntData(AN_INT);
            this.aDouble = smashableBuilder.getDoubleData(A_DOUBLE);
        }

        public String getaString() {
            return aString;
        }

        public void setaString(String aString) {
            this.aString = aString;
        }

        public int getAnInt() {
            return anInt;
        }

        public void setAnInt(int anInt) {
            this.anInt = anInt;
        }

        public double getaDouble() {
            return aDouble;
        }

        public void setaDouble(double aDouble) {
            this.aDouble = aDouble;
        }

        @Override
        public Smashable newInstance() {
            return new TestSmashable();
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
