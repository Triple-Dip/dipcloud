package org.tripledip.diana.game.captain.encounter;

import org.tripledip.diana.game.captain.consequence.AbstractConsequence;
import org.tripledip.diana.game.smashables.Challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wolfe on 4/28/2015.
 */
public abstract class AbstractEncounter {

    public final String NAME;

    protected final List<Challenge> challenges = new ArrayList<>();

    // TODO: I don't know if using two maps feels right or if the right answer is to have 2 fields
    // TODO: on the challenge class itself - but if we want failure and success to have their own
    // TODO: independent consequences then our choices are limited but maybe I've been doing this
    // TODO: too long and can't see an obvious choice right in front of me idk leave me alone!!
    protected final Map<String, AbstractConsequence>  failureConsequences = new HashMap<>();
    protected final Map<String, AbstractConsequence>  successConsequences = new HashMap<>();

    public AbstractEncounter(String name){
        NAME = name;
    }

    public abstract void onCompleted();

    public List<Challenge> getChallenges() {
        return challenges;
    }
}
