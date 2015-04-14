package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;

import java.util.List;

/**
 * Created by Wolfe on 4/11/2015.
 */
public class GameCore {

    private GameEventListeners gameEventListeners;
    private Ship ship;
    private ComlinkMessage comlinkMessage;
    private DipAccess dipAccess;


    public GameCore (){

        this.gameEventListeners = new GameEventListeners();

        // Start the game with a fresh non-destroyed ship with max hp
        ship = new Ship(false, GameConstants.SHIP_MAX_HP);

        // Start with a default message
        comlinkMessage = new ComlinkMessage(GameConstants.COMLINK_DEFAULT_MSG);

    }

    // propse damage to the dip
    public void proposeDamageShip(){



    }

    // set actual damage, fire listeners
    private void damageShip(){

    }

    public void proposeDestroyShip(){

    }

    private void destroyShip(){

    }

    public void sendComlinkMessage(){

    }

    public GameEventListeners getGameEventListeners(){
        return this.gameEventListeners;
    }

    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
    }
}
