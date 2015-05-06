package org.tripledip.diana.game.smashables;

import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;

/**
 * Created by Wolfe on 4/26/2015.
 */
public class Challenge extends Smashable{

    public static final String TYPE_BUTTON = "button";
    public static final String TYPE_LONG_PRESS = "longPress";
    public static final String TYPE_NUMBER_GUESS = "numberGuess";

    public static final String RESULT_SUCCESS = "passed";
    public static final String RESULT_FAILED = "failed";

    private static String NAME = "name";
    private static String OWNER = "owner";
    private static String RESULT = "result";
    private static String ENCOUNTER = "encounter";
    private static String TYPE = "type";

    // the unique name of the challenge to differentiate it from other challenges of the same type
    private String name;

    // the crew player owner of the challenge
    private String owner;

    // the pass or fail (or maybe something inbetween) result of the challenge
    private String result;

    // the encounter owner of this challenge
    private String encounter;

    // the type of the challenge that will determine the minigame to be wired up
    private String type;

    public Challenge(){
        // need dat empty constructor fo'dat templatez
    }

    public Challenge(String name, String encounter, String type){
        this.name = name;
        this.encounter = encounter;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEncounter() {
        return encounter;
    }

    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void smashMe(SmashableBuilder smashableBuilder) {

        smashableBuilder.addStringData(NAME, name);
        smashableBuilder.addStringData(OWNER, owner);
        smashableBuilder.addStringData(RESULT, result);
        smashableBuilder.addStringData(ENCOUNTER, encounter);
        smashableBuilder.addStringData(TYPE, type);

    }

    @Override
    public void unsmashMe(SmashableBuilder smashableBuilder) {

        name = smashableBuilder.getStringData(NAME);
        owner = smashableBuilder.getStringData(OWNER);
        result = smashableBuilder.getStringData(RESULT);
        encounter = smashableBuilder.getStringData(ENCOUNTER);
        type = smashableBuilder.getStringData(TYPE);

    }

    @Override
    public Smashable newInstance() {
        return new Challenge();
    }

    //TODO: put this in smashable
    @Override
    public boolean equals(Object obj){

        if (obj == this)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (obj instanceof Challenge)
        {
            Challenge other = (Challenge)obj;
            return other.getInstanceId().equals(getInstanceId());

        } else {
            return false;
        }

    }
}
