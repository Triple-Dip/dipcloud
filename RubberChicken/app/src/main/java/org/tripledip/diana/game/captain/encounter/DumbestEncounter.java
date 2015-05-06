package org.tripledip.diana.game.captain.encounter;

import org.tripledip.diana.game.captain.consequence.ConsequenceRegistry;
import org.tripledip.diana.game.smashables.Challenge;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class DumbestEncounter extends AbstractEncounter{

    private final String fireInEngineRoom = "Fire in Engine Room!";
    private final String gravityHasFailed = "Artificial Gravity has Failed!";
    private final String shieldsHaveFailed = "Shields have Failed!";
    private final String airlockIsJammed = "Airlock is Jammed!";
    private final String reactorIsCritical = "Reactor is Critical!";
    private final String parasiteIsSpreading = "Parasite is Spreading!";
    private final String radarIsDown = "Radar is Down!";
    private final String enemyShipIncoming = "Enemy Ship Incoming!";

    public DumbestEncounter() {
        super("DumbestEncounter");


        challenges.add(new Challenge(fireInEngineRoom, NAME, Challenge.TYPE_BUTTON));
        challenges.add(new Challenge(gravityHasFailed, NAME, Challenge.TYPE_LONG_PRESS));
        challenges.add(new Challenge(shieldsHaveFailed, NAME, Challenge.TYPE_LONG_PRESS));
        challenges.add(new Challenge(airlockIsJammed, NAME, Challenge.TYPE_BUTTON));
        challenges.add(new Challenge(reactorIsCritical, NAME, Challenge.TYPE_NUMBER_GUESS));
        challenges.add(new Challenge(parasiteIsSpreading, NAME, Challenge.TYPE_BUTTON));
        challenges.add(new Challenge(radarIsDown, NAME, Challenge.TYPE_LONG_PRESS));
        challenges.add(new Challenge(enemyShipIncoming, NAME, Challenge.TYPE_NUMBER_GUESS));


        failureConsequences.put(fireInEngineRoom, new ConsequenceRegistry.DamageShipConsequence());
        failureConsequences.put(gravityHasFailed, new ConsequenceRegistry.DepleteShipEnergyConsequence());
        failureConsequences.put(shieldsHaveFailed, new ConsequenceRegistry.TurnOffShipShieldConsequence());
        failureConsequences.put(airlockIsJammed, new ConsequenceRegistry.DepleteShipEnergyConsequence());
        failureConsequences.put(reactorIsCritical, new ConsequenceRegistry.DepleteShipEnergyConsequence());
        failureConsequences.put(parasiteIsSpreading, new ConsequenceRegistry.DamageShipConsequence());
        failureConsequences.put(radarIsDown, new ConsequenceRegistry.DamageShipConsequence());
        failureConsequences.put(enemyShipIncoming, new ConsequenceRegistry.HeavyDamageShipConsequence());

        successConsequences.put(fireInEngineRoom, new ConsequenceRegistry.RepairShipConsequence());
        successConsequences.put(gravityHasFailed, new ConsequenceRegistry.RechargeShipEnergyConsequence());
        successConsequences.put(shieldsHaveFailed, new ConsequenceRegistry.RepairShipShieldConsequence());
        successConsequences.put(airlockIsJammed, new ConsequenceRegistry.RepairShipShieldConsequence());
        successConsequences.put(reactorIsCritical, new ConsequenceRegistry.RepairShipConsequence());
        successConsequences.put(parasiteIsSpreading, new ConsequenceRegistry.RepairShipConsequence());
        successConsequences.put(radarIsDown, new ConsequenceRegistry.RepairShipShieldConsequence());
        successConsequences.put(enemyShipIncoming, new ConsequenceRegistry.RepairShipShieldConsequence());

    }

    @Override
    public void onCompleted() {

    }
}
