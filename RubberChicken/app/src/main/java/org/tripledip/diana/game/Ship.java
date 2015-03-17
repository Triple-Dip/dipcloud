package org.tripledip.diana.game;

/**
 * Created by Wolfe on 3/17/2015.
 */
public class Ship {

    public static final String SHIP = "ship";
    public static final String SHIP_HP = "shipHp";
    public static final String SHIP_ENERGY = "shipEnergy";
    public static final String SHIP_AMMO = "shipAmmo";
    public static final String SHIP_SHIELD = "shipShield";
    public static final String SHIP_STATUS = "shipStatus";

    int hp;
    int ammo;
    int energy;
    double shield;
    String status;

    public Ship(int hp, int ammo, int energy, double shield, String status) {
        this.hp = hp;
        this.ammo = ammo;
        this.energy = energy;
        this.shield = shield;
        this.status = status;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public double getShield() {
        return shield;
    }

    public void setShield(double shield) {
        this.shield = shield;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
