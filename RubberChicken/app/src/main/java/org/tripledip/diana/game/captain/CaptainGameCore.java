package org.tripledip.diana.game.captain;

import org.tripledip.diana.game.AbstractGameCore;
import org.tripledip.diana.game.Player;
import org.tripledip.diana.game.captain.encounter.EncounterController;
import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class CaptainGameCore extends AbstractGameCore {

    private final CaptainChallengeHelper challengeHelper;
    private final CaptainShipHelper shipHelper;
    private final EncounterController encounterHelper;

    public CaptainGameCore(DipAccess dipAccess, Player player){
        super(dipAccess, player);
        challengeHelper = new CaptainChallengeHelper(dipAccess, player);
        shipHelper = new CaptainShipHelper(dipAccess, player);
        encounterHelper = new EncounterController();
    }

    public CaptainChallengeHelper getChallengeHelper() {
        return challengeHelper;
    }

    public CaptainShipHelper getShipHelper() {
        return shipHelper;
    }

    public EncounterController getEncounterHelper() {
        return encounterHelper;
    }
}
