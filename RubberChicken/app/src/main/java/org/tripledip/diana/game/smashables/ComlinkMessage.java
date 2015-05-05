package org.tripledip.diana.game.smashables;

import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.contract.SmashableBuilder;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class ComlinkMessage extends Smashable{

    public static final String MESSAGE = "message";
    public static final String COLOR = "color";

    private String message;
    private int color;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void smashMe(SmashableBuilder smashableBuilder) {
        smashableBuilder.addStringData(MESSAGE, message);
        smashableBuilder.addIntData(COLOR, color);
    }

    @Override
    public void unsmashMe(SmashableBuilder smashableBuilder) {
        message = smashableBuilder.getStringData(MESSAGE);
        color = smashableBuilder.getIntData(COLOR);
    }

    @Override
    public Smashable newInstance() {
        return new ComlinkMessage();
    }
}
