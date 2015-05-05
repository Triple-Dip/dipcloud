package org.tripledip.diana.game;

import org.tripledip.diana.game.captain.ComlinkController;
import org.tripledip.diana.game.captain.encounter.EncounterController;
import org.tripledip.diana.game.crew.ChallengeHelper;
import org.tripledip.diana.game.crew.ComlinkHelper;
import org.tripledip.diana.game.crew.ShipHelper;
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
public class GameCore {

    private final ShipHelper shipHelper;
    private final ChallengeHelper challengeHelper;
    private final ComlinkHelper comlinkHelper;
    private final EncounterController encounterController;
    private final ComlinkController comlinkController;
    private final Player player;
    private final DipAccess dipAccess;

    public GameCore(DipAccess dipAccess, Player player){

        this.dipAccess = dipAccess;
        this.player = player;
        shipHelper = new ShipHelper(dipAccess);
        challengeHelper = new ChallengeHelper(dipAccess, player);
        comlinkHelper = new ComlinkHelper(dipAccess);
        encounterController = new EncounterController(this);
        comlinkController = new ComlinkController(this);

    }

    public ShipHelper getShipHelper() {
        return shipHelper;
    }

    public ChallengeHelper getChallengeHelper() {
        return challengeHelper;
    }

    public ComlinkHelper getComlinkHelper() {
        return comlinkHelper;
    }

    public EncounterController getEncounterController() {
        return encounterController;
    }

    public ComlinkController getComlinkController() {
        return comlinkController;
    }

    public Player getPlayer() {
        return player;
    }

    public DipAccess getDipAccess() {
        return dipAccess;
    }
}
