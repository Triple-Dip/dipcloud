package org.tripledip.diana.game;

/**
 * Created by Wolfe on 4/28/2015.
 * <p/>
 * This class will house the basic information about players so we have a place
 * to get things like the "owners" of challenges and the names in ComlinkMessages passed between
 * tablets.
 * <p/>
 * Every GameCore will have a reference to the Player of that tablet
 *
 */
public class Player {

    private final String name;
    private String ip;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
