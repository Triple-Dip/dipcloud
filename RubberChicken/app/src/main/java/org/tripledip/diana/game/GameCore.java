package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;

import java.util.List;

/**
 * Created by Wolfe on 4/11/2015.
 */
public class GameCore implements ScrudListener<Atom>{

    // Dipstuff
    private DipAccess dipAccess;

    // Event Listeners
    private GameEventListener<Integer> onShipDamagedListener;

    // Game Fields
    private int shipHp;
    private boolean shipDestroyed;


    public GameCore (){
        //start fresh
        shipHp = GameConstants.SHIP_MAX_HP;
        shipDestroyed = false;
    }

    // propse damage to the dip
    public void proposeDamageShip(int damage){
        // get copy of current ship
        // set current ship hp to currentHp - damage
        // smash ship and send to dipaccess propose update


    }

    // set actual damage, fire listeners
    private void damageShip(int damage){

    }

    // propose ship is destroyed
    public void proposeDestroyShip(){

    }

    // set ship to destroyed, fire listeners
    private void destroyShip(){

    }

    // send message to dip and fire listeners
    public void sendComlinkMessage(){

    }

    public void setOnShipDamagedListener(GameEventListener<Integer> onShipDamagedListener) {
        this.onShipDamagedListener = onShipDamagedListener;
    }

    /**
     *
     *
     *
     *
     *
     *
     */


    @Override
    public void onAdded(Atom thing) {

    }

    @Override
    public void onUpdated(Atom thing) {
        // compare ship hp to current hp
        // if lower, call damage with difference
        // if negative call destroy ship
    }

    @Override
    public void onRemoved(Atom thing) {

    }

    @Override
    public void onSent(Atom thing) {
        // set incoming comlink message in ui
    }

    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
    }
}
