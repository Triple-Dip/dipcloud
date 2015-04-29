package org.tripledip.diana.game.captain;

import org.tripledip.diana.game.AbstractHelper;
import org.tripledip.diana.game.Player;
import org.tripledip.diana.game.smashables.Ship;
import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class CaptainShipHelper extends AbstractHelper<Ship>{

    private Ship theShip;

    public CaptainShipHelper(DipAccess dipAccess, Player player) {
        super(dipAccess, player, new Ship());
    }

    public Ship getTheShip() {
        return theShip;
    }

    @Override
    public void onAdded(Ship ship) {
        theShip = ship;
    }

    @Override
    public void onUpdated(Ship ship) {
        theShip = ship;
    }

    @Override
    public void onRemoved(Ship ship) {

    }

    @Override
    public void onSent(Ship ship) {

    }
}
