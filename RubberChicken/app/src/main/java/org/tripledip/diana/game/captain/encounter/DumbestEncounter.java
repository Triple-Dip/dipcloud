package org.tripledip.diana.game.captain.encounter;

import org.tripledip.diana.game.captain.consequence.ConsequenceRegistry;
import org.tripledip.diana.game.smashables.Challenge;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class DumbestEncounter extends AbstractEncounter{

    private final String firstDumbestChallengeName = "dumbChallenge";
    private final String secondDumbestChallengeName = "dumberChallenge";

    public DumbestEncounter() {
        super("DumbestEncounter");


        challenges.add(new Challenge(firstDumbestChallengeName, NAME, Challenge.TYPE_DUMB));
        challenges.add(new Challenge(secondDumbestChallengeName, NAME, Challenge.TYPE_STUPID));

        // this whole mapping seems really gross and should probably change.
        // but I wanted to try it anyway!
        ConsequenceRegistry.DamageShipConsequence  damageShipConsequence =
                new ConsequenceRegistry.DamageShipConsequence();

        ConsequenceRegistry.RepairShipConsequence  repairShipConsequence =
                new ConsequenceRegistry.RepairShipConsequence();

        failureConsequences.put(firstDumbestChallengeName, damageShipConsequence);
        failureConsequences.put(secondDumbestChallengeName, damageShipConsequence);

        successConsequences.put(firstDumbestChallengeName, repairShipConsequence);
        successConsequences.put(secondDumbestChallengeName, repairShipConsequence);

    }

    @Override
    public void onCompleted() {

    }
}
