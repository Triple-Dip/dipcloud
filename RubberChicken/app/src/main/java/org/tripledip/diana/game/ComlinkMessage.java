package org.tripledip.diana.game;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class ComlinkMessage {

    public static final String DEFAULT_MSG = "Boot strapped!";
    public static final String MESSAGE = "comlinkMessage";

    public ComlinkMessage(String message){
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
