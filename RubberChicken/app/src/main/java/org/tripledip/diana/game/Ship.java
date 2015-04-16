package org.tripledip.diana.game;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class Ship {

    public static final int MAX_HP = 10;
    public static final String HP = "shipHp";
    public static final String DESTROYED = "shipDestroyed";

    public Ship(int shipHp, boolean shipDestroyed){
        this.shipHp = shipHp;
        this.shipDestroyed = shipDestroyed;
    }
    // Game Fields
    private int shipHp;
    private boolean shipDestroyed;

    public int getShipHp() {
        return shipHp;
    }

    public void setShipHp(int shipHp) {
        this.shipHp = shipHp;
    }

    public boolean isShipDestroyed() {
        return shipDestroyed;
    }

    public void setShipDestroyed(boolean shipDestroyed) {
        this.shipDestroyed = shipDestroyed;
    }
}
