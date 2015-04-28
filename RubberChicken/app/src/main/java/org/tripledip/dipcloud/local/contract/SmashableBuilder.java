package org.tripledip.dipcloud.local.contract;

import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/27/2015.
 */
public class SmashableBuilder {

    private final long sequenceNumber;
    private final Molecule molecule;

    public SmashableBuilder (long sequenceNumber, Molecule molecule){

        this.sequenceNumber = sequenceNumber;
        this.molecule = molecule;

    }

    public void addStringData(String key, String data){
        molecule.addOrReplace(new Atom(instanceify(key),sequenceNumber, data));
    }
    public void addIntData(String key, int data){
        molecule.addOrReplace(new Atom(instanceify(key),sequenceNumber, data));
    }
    public void addDoubleData(String key, double data){
        molecule.addOrReplace(new Atom(instanceify(key),sequenceNumber ,data));
    }


    public String getStringData(String key){
        return molecule.findById(instanceify(key)).getStringData();
    }

    public int getIntData(String key){
        return molecule.findById(instanceify(key)).getIntData();
    }

    public double getDoubleData(String key){
        return molecule.findById(instanceify(key)).getDoubleData();
    }

    private String instanceify(String key){
        return molecule.getInstanceId() + key;
    }

    public Molecule getMolecule() {
        return molecule;
    }
}
