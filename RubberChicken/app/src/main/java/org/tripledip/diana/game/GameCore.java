package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/11/2015.
 *
 * This class is supposed to be the bread and butter brain of the game. The idea is to have all the
 * events of the game: Damage/Repair Ship, Charge Shields, Destroy Ship, etc etc housed in
 * this class.
 *
 * It acts as the intermediate between the DipAccess and the UI.
 *
 * Input from the player is taken from the UI which calls methods that propose changes to the
 * DipAccess.
 *
 * This class is the listener for changes in the NimBase/Dip - these changes come in through the
 * interfaced methods which compare the changes to the current values. These changes are then
 * set locally by calling the relevant event methods. For example if the incoming ship atoms had
 * lower hp compared to the current ship, the damageShip() method would be called with the
 * difference.
 */
public class GameCore implements ScrudListener<Molecule> {

    // Dip Stuff
    private DipAccess dipAccess;

    // Event Listeners
    private GameEventListener<Ship> onShipDamagedListener;
    private GameEventListener<Ship> onShipDestroyedListener;
    private GameEventListener<ComlinkMessage> onMessageSentListener;

    // Game Objects
    Ship currentShip;


    public GameCore (){

        // start fresh
        currentShip = new Ship(Ship.MAX_HP, false);

    }

    // propose damage to the dip
    public void proposeDamageShip(int damage){

        //TODO:
        // get copy of current currentShip
        // set current currentShip hp to currentHp - damage
        // smash currentShip and send to dipaccess propose update


        //Right now we're just using a molecule for the currentShip
        Atom shipHpAtom = new Atom(Ship.HP,
                dipAccess.getNimbase().nextSequenceNumber(),
                currentShip.getShipHp() - damage);
        dipAccess.proposeUpdate(new Molecule(Ship.class.getName(), shipHpAtom));


    }

    // set actual damage, fire listeners
    private void damageShip(int damage){
        currentShip.setShipHp(currentShip.getShipHp()-damage);
        onShipDamagedListener.onEventOccurred(currentShip);
        if(currentShip.getShipHp() <= 0){
            destroyShip();
        }
    }

    // propose currentShip is destroyed
    public void proposeDestroyShip(){

    }

    // set currentShip to destroyed, fire listeners
    private void destroyShip(){
        currentShip.setShipDestroyed(true);
        onShipDestroyedListener.onEventOccurred(currentShip);
    }

    // send message to dip
    public void sendComlinkMessage(String message){

        dipAccess.proposeSend(new Molecule(ComlinkMessage.class.getName(), new Atom(ComlinkMessage.MESSAGE, 1, message)));

    }

    public void setOnShipDamagedListener(GameEventListener<Ship> onShipDamagedListener) {
        this.onShipDamagedListener = onShipDamagedListener;
    }

    public void setOnShipDestroyedListener(GameEventListener<Ship> onShipDestroyedListener) {
        this.onShipDestroyedListener = onShipDestroyedListener;
    }

    public void setOnMessageSentListener(GameEventListener<ComlinkMessage> onMessageSentListener) {
        this.onMessageSentListener = onMessageSentListener;
    }

    /**
     *
     *
     *
     *      Dippy Stuff
     *
     *
     */


    @Override
    public void onAdded(Molecule thing) {

    }

    @Override
    public void onUpdated(Molecule thing) {
        // compare currentShip hp to current hp
        // if lower, call damage with difference
        // if negative call destroy currentShip

        int currentHp = currentShip.getShipHp();
        int updatedHp = thing.findById(Ship.HP).getIntData();


        if(updatedHp < currentHp){
            int difference = currentHp - updatedHp;
            damageShip(difference);
        }



    }

    @Override
    public void onRemoved(Molecule thing) {

    }

    @Override
    public void onSent(Molecule thing) {

        // set incoming comlink message in ui
        onMessageSentListener.onEventOccurred(new ComlinkMessage(thing.findById(ComlinkMessage.MESSAGE).getStringData()));

    }

    //TODO: should wiring up the ScrudListeners be done here? Seems better in its own method
    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
        dipAccess.getChannelListeners().registerListener(ComlinkMessage.class.getName(),this);
        dipAccess.getChannelListeners().registerListener(Ship.class.getName(),this);
    }

    //TODO: Right now this is a hack to solve the problem of how we get the initial atoms in the dip
    public void bootStrapGame(){

        dipAccess.proposeAdd(new Molecule(Ship.class.getName(),
                new Atom(Ship.HP, 0, currentShip.getShipHp())));

    }
}
