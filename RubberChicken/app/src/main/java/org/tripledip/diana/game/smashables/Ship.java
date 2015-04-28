package org.tripledip.diana.game.smashables;

import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;
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

    public int getShipHp() {
        return shipHp;
    }

    public int getShipShield() {
        return shipShield;
    }

    public int getShipEnergy() {
        return shipEnergy;
    }

    public void setShipHp(int shipHp) {
        this.shipHp = shipHp;
    }

    public void setShipShield(int shipShield) {
        this.shipShield = shipShield;
    }

    public void setShipEnergy(int shipEnergy) {
        this.shipEnergy = shipEnergy;
    }


    @Override
    public void smashMe(SmashableBuilder smashableBuilder) {

    }

    @Override
    public void unsmashMe(SmashableBuilder smashableBuilder) {

    }

    @Override
    public Smashable newInstance() {
        return new Ship();
    }
}
