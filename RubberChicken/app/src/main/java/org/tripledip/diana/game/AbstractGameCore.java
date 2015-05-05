package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfe on 4/28/2015.
 */
public abstract class AbstractGameCore {

    private final DipAccess dipAccess;
    private final Player player;
    protected final Map<String, AbstractHelper> helpers = new HashMap<>();

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

    public Map<String, AbstractHelper> getHelpers() {
        return helpers;
    }
}
