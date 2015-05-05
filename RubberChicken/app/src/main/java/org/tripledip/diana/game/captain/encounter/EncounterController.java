package org.tripledip.diana.game.captain.encounter;

import org.tripledip.diana.game.GameCore;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.dipcloud.local.contract.ScrudListener;

import java.util.List;

/**
 * Created by Wolfe on 4/28/2015.
 *
 * I called it controller because helpers seemed to be 1 to 1 with the smashables and this isn't
 *
 * However, it does act much like a helper for challenges, so much so that depending on
 * how little the actual CaptainChallengeHelper ends up doing, it might be worth just chucking it
 *
 */
public class EncounterController implements ScrudListener<Challenge> {

    private final GameCore gameCore;

    // TODO: could eventually be a list of ongoing encounters... ?
    private AbstractEncounter currentEncounter;

    public EncounterController(GameCore gameCore){
        this.gameCore = gameCore;
    }

    // called when the captain clicks the encounter in the ListView of encounters
    public void startEncounter(AbstractEncounter encounter){
        this.currentEncounter = encounter;

        // add the encounter's challenges to the dip
        List<Challenge> challenges = encounter.getChallenges();
        for(Challenge challenge : challenges) {
            gameCore.getDipAccess().proposeAdd(challenge);
        }

        // TODO: we need to figure out how to best leverage the ComlinkMessage to notify people

    }

    @Override
    public void onAdded(Challenge challenge) {

    }

    @Override
    public void onUpdated(Challenge challenge) {

        String result = challenge.getResult();
        if(null != result){

            if(result.equals(Challenge.RESULT_SUCCESS)){
                currentEncounter.successConsequences.get(challenge.getName()).doStuff(gameCore);
            }

            if(result.equals(Challenge.RESULT_FAILED)){
                currentEncounter.failureConsequences.get(challenge.getName()).doStuff(gameCore);
            }

            // since we are the one removing it and the owner isn't null at this point, we should be
            // able to send out a ComlinkMessage saying 'XYZ player passed/failed XYZ challenge!'
            // so cool!
            gameCore.getDipAccess().proposeRemove(challenge);

        }

    }

    @Override
    public void onRemoved(Challenge challenge) {

    }

    @Override
    public void onSent(Challenge challenge) {

    }
}
