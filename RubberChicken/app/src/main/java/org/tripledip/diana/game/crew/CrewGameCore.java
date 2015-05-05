package org.tripledip.diana.game.crew;

import org.tripledip.diana.game.AbstractGameCore;
import org.tripledip.diana.game.Player;
import org.tripledip.dipcloud.local.contract.DipAccess;

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
public class CrewGameCore extends AbstractGameCore{

    private final ShipHelper shipHelper;
    private final CrewChallengeHelper challengeHelper;
    private final ComlinkHelper comlinkHelper;

    public CrewGameCore(DipAccess dipAccess, Player player){
        super(dipAccess, player);

        shipHelper = new ShipHelper(dipAccess, player);
        helpers.put(ShipHelper.class.getName(), shipHelper);

        challengeHelper = new CrewChallengeHelper(dipAccess, player);
        helpers.put(CrewChallengeHelper.class.getName(), challengeHelper);

        comlinkHelper = new ComlinkHelper(dipAccess, player);
        helpers.put(ComlinkHelper.class.getName(), comlinkHelper);

    }

    public ShipHelper getShipHelper() {
        return shipHelper;
    }

    public CrewChallengeHelper getChallengeHelper() {
        return challengeHelper;
    }
}
