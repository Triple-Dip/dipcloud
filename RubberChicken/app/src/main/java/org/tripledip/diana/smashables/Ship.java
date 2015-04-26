package org.tripledip.diana.smashables;

import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class Ship extends Smashable {

    public static final String HP = "shipHp";
    public static final int MAX_HP = 10;
    public static final int MIN_HP = 0;

    public static final String SHIELD = "shipShield";
    public static final int MAX_SHIELD = 100;
    public static final int MIN_SHIELD = 0;

    public static final String ENERGY = "shipEnergy";
    public static final int MAX_ENERGY = 20;
    public static final int MIN_ENERGY = 0;

    // Ship Fields
    private int shipHp;
    private int shipShield;
    private int shipEnergy;

    public Ship(){
        // Empty constructor for smashable template
    }

    public Ship(int shipHp, int shipShield, int shipEnergy){
        this.shipHp = shipHp;
        this.shipShield = shipShield;
        this.shipEnergy = shipEnergy;
    }

    public int getShipHp() {
        return shipHp;
    }

    public int getShipShield() {
        return shipShield;
    }

    public int getShipEnergy() {
        return shipEnergy;
    }

    @Override
    public void smashMe(Molecule molecule, long sequenceNumber) {
        molecule.addOrReplace(new Atom(HP,sequenceNumber, shipHp));
        molecule.addOrReplace(new Atom(ENERGY,sequenceNumber, shipEnergy));
        molecule.addOrReplace(new Atom(SHIELD,sequenceNumber, shipShield));
    }

    @Override
    public void unsmashMe(Molecule molecule) {
        shipHp = molecule.findById(HP).getIntData();
        shipEnergy = molecule.findById(ENERGY).getIntData();
        shipShield = molecule.findById(SHIELD).getIntData();
    }

    @Override
    public Smashable newInstance() {
        return new Ship();
    }
}
