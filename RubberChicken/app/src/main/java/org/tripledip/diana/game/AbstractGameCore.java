package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 4/28/2015.
 */
public abstract class AbstractGameCore {

    private final DipAccess dipAccess;
    private final Player player;

    public AbstractGameCore(DipAccess dipAccess, Player player){
        this.dipAccess = dipAccess;
        this.player = player;
    }

    public DipAccess getDipAccess() {
        return dipAccess;
    }

    public Player getPlayer() {
        return player;
    }
}
