package org.tripledip.diana.game.captain.consequence;

import org.tripledip.diana.game.GameCore;
import org.tripledip.diana.game.smashables.Ship;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class ConsequenceRegistry {

    public static class DamageShipConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());

            int damage = 9;
            int leftOver = ship.getShipShield() - damage;


            if((ship.getShipShield() - damage) < Ship.MIN_ENERGY){
                ship.setShipShield(Ship.MIN_ENERGY);
            } else {
                ship.setShipShield(ship.getShipShield() - damage);
            }

            if(leftOver < 0){
                if((ship.getShipHp() + leftOver) < Ship.MIN_HP){
                    ship.setShipHp(Ship.MIN_HP);
                } else {
                    ship.setShipHp(ship.getShipHp() + leftOver);
                }
            }

            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }

    public static class HeavyDamageShipConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());

            int damage = 15;
            int leftOver = ship.getShipShield() - damage;


            if((ship.getShipShield() - damage) < Ship.MIN_ENERGY){
                ship.setShipShield(Ship.MIN_ENERGY);
            } else {
                ship.setShipShield(ship.getShipShield() - damage);
            }

            if(leftOver < 0){
                if((ship.getShipHp() + leftOver) < Ship.MIN_HP){
                    ship.setShipHp(Ship.MIN_HP);
                } else {
                    ship.setShipHp(ship.getShipHp() + leftOver);
                }
            }




            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }


    public static class TurnOffShipShieldConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());

            ship.setShipShield(Ship.MIN_SHIELD);

            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }

    public static class RepairShipShieldConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());

            int repairAmount = 8;

            if(ship.getShipEnergy() < Ship.MAX_ENERGY/2){
                repairAmount = 3;
            }

            if((ship.getShipShield() + repairAmount) > Ship.MAX_SHIELD){
                ship.setShipShield(Ship.MAX_SHIELD);
            } else {
                ship.setShipShield(ship.getShipShield() + repairAmount);
            }

            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }

    public static class RepairShipConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());
            if((ship.getShipHp() + 8) > Ship.MAX_HP){
                ship.setShipHp(Ship.MAX_HP);
            } else {
                ship.setShipHp(ship.getShipHp() + 8);
            }
            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }


    public static class DepleteShipEnergyConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());

            if((ship.getShipEnergy() - 8) < Ship.MIN_ENERGY){
                ship.setShipEnergy(Ship.MIN_ENERGY);
            } else {
                ship.setShipEnergy(ship.getShipEnergy() - 3);
            }

            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }



    public static class RechargeShipEnergyConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());

            if((ship.getShipEnergy() + 4) > Ship.MAX_ENERGY){
                ship.setShipEnergy(Ship.MAX_ENERGY);
            } else {
                ship.setShipEnergy(ship.getShipEnergy() + 4);
            }

            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }

    private static Ship copyShip(Ship ship){

        Ship newShip = new Ship();
        newShip.setInstanceId(ship.getInstanceId());
        newShip.setShipEnergy(ship.getShipEnergy());
        newShip.setShipHp(ship.getShipHp());
        newShip.setShipShield(ship.getShipShield());

        return newShip;
    }

}
