package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.List;

/**
 * Created by Wolfe on 4/11/2015.
 */
public class GameCore implements ScrudListener<Atom>{

    // Dip Stuff
    private DipAccess dipAccess;

    // Event Listeners
    private GameEventListener<Ship> onShipDamagedListener;
    private GameEventListener<ComlinkMessage> onMessageSentListener;

    // Game Objects
    Ship ship;


    public GameCore (){

        // start fresh
        ship = new Ship(Ship.MAX_HP, false);

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

    // send message to dip
    public void sendComlinkMessage(String message){

        dipAccess.proposeSend(new Molecule(ComlinkMessage.class.getName(), new Atom(ComlinkMessage.MESSAGE, 1, message)));

    }

    public void setOnShipDamagedListener(GameEventListener<Ship> onShipDamagedListener) {
        this.onShipDamagedListener = onShipDamagedListener;
    }

    public void setOnMessageSentListener(GameEventListener<ComlinkMessage> onMessageSentListener) {
        this.onMessageSentListener = onMessageSentListener;
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
        onMessageSentListener.onEventOccurred(new ComlinkMessage(thing.getStringData()));

    }

    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
        dipAccess.getIdListeners().registerListener(ComlinkMessage.MESSAGE,this);
    }

    public void bootStrapGame(){

        dipAccess.proposeAdd(new Molecule(Ship.class.getName(),
                new Atom(Ship.HP, 0, ship.getShipHp())));

        dipAccess.proposeSend(new Molecule(ComlinkMessage.class.getName(),
                new Atom(ComlinkMessage.MESSAGE, 0, ComlinkMessage.DEFAULT_MSG)));

    }
}
