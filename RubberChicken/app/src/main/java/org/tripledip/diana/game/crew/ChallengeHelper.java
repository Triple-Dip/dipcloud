package org.tripledip.diana.game.crew;

import org.tripledip.diana.game.AbstractHelper;
import org.tripledip.diana.game.Player;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wolfe on 4/26/2015.
 */
public class ChallengeHelper extends AbstractHelper<Challenge>{

    public static final String EVENT_ADD_CHALLENGE= "addChallengeEvent";
    public static final String EVENT_REMOVE_CHALLENGE = "removeChallengeEvent";
    public static final String EVENT_FINISH_CHALLENGE = "finishChallengeEvent";
    public static final String EVENT_START_CHALLENGE = "startChallengeEvent";

    private final List<Challenge> challenges = new ArrayList<>();
    private Challenge currentChallenge;
    private Player player;

    public ChallengeHelper(DipAccess dipAccess, Player player) {
        super(dipAccess, new Challenge());
        this.player = player;

    }

    // set owner to the player's name and propose
    public void requestOwnership(Challenge challenge){
        challenge.setOwner(player.getName());
        dipAccess.proposeUpdate(challenge);
    }

    // if the incoming challenge is the player's name, start it
    private void verifyOwnership(Challenge challenge){
        String owner = challenge.getOwner();
        String result = challenge.getResult();

        // someone definitely owns it now and it's finished
        if(null != owner && null == result ){

            // it's me!!
            if(owner.equals(player.getName())){
                startChallenge(challenge);
            } else {
                // or not :(
                removeChallenge(challenge);
            }

        }

    }

    // start challenge is a game event... !
    private void startChallenge(Challenge challenge){
        currentChallenge = challenge;
        gameEventNotifier.notifyEventOccurred(EVENT_START_CHALLENGE, challenge);
        //TODO: wire up the fragment minigame and start playin'

    }

    // challenge is finished!
    private void finishChallenge(Challenge challenge){

        dipAccess.proposeUpdate(challenge);
        currentChallenge = null;
        removeChallenge(challenge);
        gameEventNotifier.notifyEventOccurred(EVENT_FINISH_CHALLENGE, challenge);

    }

    // challenges get added from an encounter
    public void addChallenge(Challenge challenge){
        challenges.add(challenge);
        gameEventNotifier.notifyEventOccurred(EVENT_ADD_CHALLENGE, challenge);
    }

    // challenges get removed from the encounter due to completion
    public void removeChallenge(Challenge challenge){
        challenges.remove(challenge);
        gameEventNotifier.notifyEventOccurred(EVENT_REMOVE_CHALLENGE, challenge);
    }

    @Override
    public void onSent(Challenge challenge) {

    }

    @Override
    public void onRemoved(Challenge challenge) {
        removeChallenge(challenge);
    }

    @Override
    public void onUpdated(Challenge challenge) {
        verifyOwnership(challenge);
    }

    @Override
    public void onAdded(Challenge challenge) {
        addChallenge(challenge);
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }
}
