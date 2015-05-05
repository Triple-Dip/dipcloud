package org.tripledip.diana.game;

/**
 * Created by Wolfe on 4/28/2015.
 *
 * This class will house the basic information about players so we have a place
 * to get things like the "owners" of challenges and the names in ComlinkMessages passed between
 * tablets.
 *
 * Every GameCore will have a reference to the Player of that tablet
 *
 * for the time being name can be ip - but it would be nice to have an option to pick a name
 * before launching the game.
 *
 */
public class Player {

    private final String name;

    public Player(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
