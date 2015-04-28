package org.tripledip.diana.game.smashables;

import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;

/**
 * Created by Wolfe on 4/26/2015.
 */
public class Challenge extends Smashable{

    private static String NAME = "name";
    private static String OWNER = "owner";
    private static String RESULT = "result";
    private static String ENCOUNTER = "encounter";
    private static String TYPE = "type";

    private String name;
    private String owner;
    private String result;
    private String encounter;
    private String type;

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

}
