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
 * TODO: explain this class
 *
 */
public class GameCore {

    private final ShipHelper shipHelper;
    private final ChallengeHelper challengeHelper;
    private final ComlinkHelper comlinkHelper;
    private final Player player;
    private final DipAccess dipAccess;
    private EncounterController encounterController;
    private ComlinkController comlinkController;

    public GameCore(DipAccess dipAccess, Player player){

        this.dipAccess = dipAccess;
        this.player = player;
        shipHelper = new ShipHelper(dipAccess);
        challengeHelper = new ChallengeHelper(dipAccess, player);
        comlinkHelper = new ComlinkHelper(dipAccess);

    }

    public void setToCaptainMode(boolean mode){

        if(mode){
            encounterController = new EncounterController(this);
            comlinkController = new ComlinkController(this);
        }

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
