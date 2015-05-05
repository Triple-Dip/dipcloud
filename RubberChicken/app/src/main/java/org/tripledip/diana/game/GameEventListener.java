package org.tripledip.diana.game;

import org.tripledip.dipcloud.local.model.Atom;

/**
 * Created by Wolfe on 4/11/2015.
 */
public interface GameEventListener<T> {

    public void onEventOccurred(String event, T thing);

}
