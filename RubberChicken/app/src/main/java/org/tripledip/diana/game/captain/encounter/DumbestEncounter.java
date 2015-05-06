package org.tripledip.diana.game.captain.encounter;

import org.tripledip.diana.game.captain.consequence.ConsequenceRegistry;
import org.tripledip.diana.game.smashables.Challenge;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class DumbestEncounter extends AbstractEncounter{

    private final String firstDumbestChallengeName = "Dumb Challenge 1";
    private final String secondDumbestChallengeName = "Dumb Challenge 2";
    private final String thirdDumbestChallengeName = "Dumb Challenge 3";
    private final String forthDumbestChallengeName = "Dumb Challenge 4";
    private final String fifthDumbestChallengeName = "Dumb Challenge 5";
    private final String sixthDumbestChallengeName = "Dumb Challenge 6";

    public DumbestEncounter() {
        super("DumbestEncounter");


        challenges.add(new Challenge(firstDumbestChallengeName, NAME, Challenge.TYPE_DUMB));
        challenges.add(new Challenge(secondDumbestChallengeName, NAME, Challenge.TYPE_DUMB));
        challenges.add(new Challenge(thirdDumbestChallengeName, NAME, Challenge.TYPE_DUMB));
        challenges.add(new Challenge(forthDumbestChallengeName, NAME, Challenge.TYPE_DUMB));
        challenges.add(new Challenge(fifthDumbestChallengeName, NAME, Challenge.TYPE_DUMB));
        challenges.add(new Challenge(sixthDumbestChallengeName, NAME, Challenge.TYPE_DUMB));

        // this whole mapping seems really gross and should probably change.
        // but I wanted to try it anyway!
        ConsequenceRegistry.DamageShipConsequence  damageShipConsequence =
                new ConsequenceRegistry.DamageShipConsequence();

        ConsequenceRegistry.RepairShipConsequence  repairShipConsequence =
                new ConsequenceRegistry.RepairShipConsequence();

        failureConsequences.put(firstDumbestChallengeName, damageShipConsequence);
        failureConsequences.put(secondDumbestChallengeName, damageShipConsequence);
        failureConsequences.put(thirdDumbestChallengeName, damageShipConsequence);
        failureConsequences.put(forthDumbestChallengeName, damageShipConsequence);
        failureConsequences.put(fifthDumbestChallengeName, damageShipConsequence);
        failureConsequences.put(sixthDumbestChallengeName, damageShipConsequence);

        successConsequences.put(firstDumbestChallengeName, repairShipConsequence);
        successConsequences.put(secondDumbestChallengeName, repairShipConsequence);
        successConsequences.put(thirdDumbestChallengeName, repairShipConsequence);
        successConsequences.put(forthDumbestChallengeName, repairShipConsequence);
        successConsequences.put(fifthDumbestChallengeName, repairShipConsequence);
        successConsequences.put(sixthDumbestChallengeName, repairShipConsequence);

    }

    @Override
    public void onCompleted() {

    }
}
