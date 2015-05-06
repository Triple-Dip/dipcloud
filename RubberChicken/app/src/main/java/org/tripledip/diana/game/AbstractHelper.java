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

    public AbstractHelper(DipAccess dipAccess, T smashable) {
        this.dipAccess = dipAccess;
        dipAccess.registerSmashable(smashable);
        dipAccess.getSmashableListeners().registerListener(smashable.getChannel(), (ScrudListener<Smashable>)this);
    }

    public GameEventNotifier<T> getGameEventNotifier() {
        return gameEventNotifier;
    }

    public void bootstrapSmashables() {
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
