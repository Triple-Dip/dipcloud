package org.tripledip.diana.game;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class Ship {

    int shipHp;
    boolean destroyed;

    public Ship(boolean destroyed, int shipHp) {
        this.destroyed = destroyed;
        this.shipHp = shipHp;
    }

    public int getShipHp() {
        return shipHp;
    }

    public void setShipHp(int shipHp) {
        this.shipHp = shipHp;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
