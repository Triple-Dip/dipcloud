package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.contract.Smashable;

/**
 * Created by Wolfe on 4/26/2015.
 */

public abstract class AbstractHelper<T extends Smashable> implements ScrudListener<T> {


    protected final GameEventNotifier<T> gameEventNotifier = new GameEventNotifier<>();
    protected final DipAccess dipAccess;
    protected final Player player;

    public AbstractHelper (DipAccess dipAccess, Player player, T smashable){
        this.player = player;
        this.dipAccess = dipAccess;
        dipAccess.registerSmashable(smashable);
    }

    public GameEventNotifier<T> getGameEventNotifier(){
        return gameEventNotifier;
    }


    @Override
    public void onAdded(T thing) {

    }

    @Override
    public void onUpdated(T thing) {

    }

    @Override
    public void onRemoved(T thing) {

    }

    @Override
    public void onSent(T thing) {

    }
}
