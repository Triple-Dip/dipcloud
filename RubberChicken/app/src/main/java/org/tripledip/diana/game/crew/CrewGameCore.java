package org.tripledip.diana.game.crew;

import org.tripledip.diana.game.AbstractGameCore;
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

    private final CrewShipHelper shipHelper;
    private final CrewChallengeHelper challengeHelper;

    public CrewGameCore(DipAccess dipAccess){
        super(dipAccess);
        shipHelper = new CrewShipHelper(dipAccess);
        challengeHelper = new CrewChallengeHelper(dipAccess);
    }

}
