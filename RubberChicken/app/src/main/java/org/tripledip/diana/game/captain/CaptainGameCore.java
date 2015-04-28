package org.tripledip.diana.game.captain;

import org.tripledip.diana.game.AbstractGameCore;
import org.tripledip.diana.game.captain.encounter.EncounterController;
import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class CaptainGameCore extends AbstractGameCore {

    private final CaptainChallengeHelper challengeHelper;
    private final CaptainShipHelper shipHelper;
    private final EncounterController encounterHelper;

    public CaptainGameCore(DipAccess dipAccess){
        super(dipAccess);
        challengeHelper = new CaptainChallengeHelper(dipAccess);
        shipHelper = new CaptainShipHelper(dipAccess);
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
