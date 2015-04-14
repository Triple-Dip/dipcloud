package org.tripledip.diana.game;

import java.util.List;

/**
 * Created by Wolfe on 4/14/2015.
 */
public class GameEventListeners {
    // These will be lists of listeners when it's not dumb
    private List<EventUiListener<Ship>> shipDamagedListeners;
    private List<EventUiListener<Ship>> shipDestroyedListeners;
    private List<EventUiListener<ComlinkMessage>> messageReceivedListeners;


    public void registerMessageReceivedListener(EventUiListener messageReceivedListener) {
        messageReceivedListeners.add(messageReceivedListener);
    }

    public void registerShipDamagedListener(EventUiListener shipDamagedListener) {
        shipDamagedListeners.add(shipDamagedListener);
    }

    public void registerShipDestroyedListener(EventUiListener shipDestroyedListener) {
        shipDestroyedListeners.add(shipDestroyedListener);
    }
}
