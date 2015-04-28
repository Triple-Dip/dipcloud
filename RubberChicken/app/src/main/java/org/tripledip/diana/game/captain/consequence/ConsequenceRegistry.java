package org.tripledip.diana.game.captain.consequence;

import org.tripledip.diana.game.captain.CaptainGameCore;
import org.tripledip.diana.game.smashables.Ship;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class ConsequenceRegistry {



    public class DamageShipConsequence extends AbstractConsequence {

        @Override
        public void doStuff(CaptainGameCore gameCore) {

            Ship ship = gameCore.getShipHelper().getTheShip();
            ship.setShipHp(ship.getShipHp() - 5);
            gameCore.getDipAccess().proposeUpdate(ship);

        }
    }


}
