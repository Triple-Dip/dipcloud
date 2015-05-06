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
            ship.setShipHp(ship.getShipHp() - 5);
            gameCore.getDipAccess().proposeUpdate(ship);

        }

    }

    public static class RepairShipConsequence extends AbstractConsequence {

        @Override
        public void doStuff(GameCore gameCore) {

            Ship ship = copyShip(gameCore.getShipHelper().getTheShip());
            ship.setShipHp(ship.getShipHp() + 5);
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
