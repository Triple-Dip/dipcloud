package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 4/28/2015.
 */
public abstract class AbstractGameCore {

    private final DipAccess dipAccess;

    public AbstractGameCore(DipAccess dipAccess){
        this.dipAccess = dipAccess;
    }

    public DipAccess getDipAccess() {
        return dipAccess;
    }

}
