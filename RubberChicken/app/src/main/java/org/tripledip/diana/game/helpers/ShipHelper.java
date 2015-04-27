package org.tripledip.diana.game.helpers;

import org.tripledip.diana.game.GameEventListener;
import org.tripledip.diana.game.GameEventNotifier;
import org.tripledip.diana.smashables.Ship;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.contract.Smashable;

/**
 * Created by Wolfe on 4/26/2015.
 */
public class ShipHelper extends AbstractHelper<Ship> {

    public static final String EVENT_DAMAGE_HP = "damageHpEvent";
    public static final String EVENT_REPAIR_HP = "repairHpEvent";
    public static final String EVENT_DAMAGE_SHIELD = "damageShieldEvent";
    public static final String EVENT_REPAIR_SHIELD = "repairShieldEvent";
    public static final String EVENT_DEPLETE_ENERGY = "depleteEnergyEvent";
    public static final String EVENT_RECHARGE_ENERGY = "rechargeEnergyEvent";

    private Ship theShip;

    public ShipHelper(DipAccess dipAccess){
        super(dipAccess);
    }

    /******************************************
                    Ship Events
     *****************************************/

    private void damageShipHp (Ship ship){
        gameEventNotifier.notifyEventOccurred(EVENT_DAMAGE_HP, ship);
    }

    private void damageShipShield (Ship ship){
        gameEventNotifier.notifyEventOccurred(EVENT_DAMAGE_SHIELD, ship);
    }

    private void repairShipHp(Ship ship){
        gameEventNotifier.notifyEventOccurred(EVENT_REPAIR_HP, ship);
    }

    private void repairShipShield(Ship ship){
        gameEventNotifier.notifyEventOccurred(EVENT_REPAIR_SHIELD, ship);
    }

    private void rechargeShipEnergy(Ship ship){
        gameEventNotifier.notifyEventOccurred(EVENT_RECHARGE_ENERGY, ship);
    }

    private void depleteShipEnergy(Ship ship){
        gameEventNotifier.notifyEventOccurred(EVENT_DEPLETE_ENERGY, ship);
    }

    /******************************************
                 Incoming Dip Stuff
     *****************************************/
    @Override
    public void onAdded(Ship ship) {
        theShip = ship;
    }
    @Override
    public void onUpdated(Ship ship) {
        computeShipAndNotify(ship);
    }

    private void computeShipAndNotify(Ship newShip){

        if(newShip.getShipHp() < theShip.getShipHp()){
            damageShipHp(newShip);
        }
        if(newShip.getShipHp() > theShip.getShipHp()){
            repairShipHp(newShip);
        }

        if(newShip.getShipShield() < theShip.getShipShield()){
            damageShipShield(newShip);
        }
        if(newShip.getShipShield() > theShip.getShipShield()){
            repairShipShield(newShip);
        }

        if(newShip.getShipEnergy() < theShip.getShipEnergy()){
            depleteShipEnergy(newShip);
        }
        if(newShip.getShipShield() > theShip.getShipShield()){
            rechargeShipEnergy(newShip);
        }

        theShip = newShip;
    }

}