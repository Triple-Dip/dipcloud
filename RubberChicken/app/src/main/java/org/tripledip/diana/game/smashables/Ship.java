package org.tripledip.diana.game.smashables;

import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class Ship extends Smashable {

    public static final String HP = "hp";
    public static final int MAX_HP = 25;
    public static final int MIN_HP = 0;

    public static final String SHIELD = "shield";
    public static final int MAX_SHIELD = 3;
    public static final int MIN_SHIELD = 0;

    public static final String ENERGY = "energy";
    public static final int MAX_ENERGY = 15;
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

        smashableBuilder.addIntData(HP, shipHp);
        smashableBuilder.addIntData(SHIELD, shipShield);
        smashableBuilder.addIntData(ENERGY, shipEnergy);

    }

    @Override
    public void unsmashMe(SmashableBuilder smashableBuilder) {
        shipHp = smashableBuilder.getIntData(HP);
        shipShield = smashableBuilder.getIntData(SHIELD);
        shipEnergy = smashableBuilder.getIntData(ENERGY);
    }

    @Override
    public Smashable newInstance() {
        return new Ship();
    }
}
