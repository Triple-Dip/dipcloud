package org.tripledip.diana.game;

/**
 * Created by Wolfe on 4/11/2015.
 */
public class GameCore {

    // These will be lists of listeners when it's not dumb
    private EventUiListener shipDamagedListener;
    private EventUiListener shipDestroyedListener;
    private EventUiListener messageReceivedListener;





    public void registerMessageReceivedListener(EventUiListener messageReceivedListener) {
        this.messageReceivedListener = messageReceivedListener;
    }

    public void registerShipDamagedListener(EventUiListener shipDamagedListener) {
        this.shipDamagedListener = shipDamagedListener;
    }

    public void registerShipDestroyedListener(EventUiListener shipDestroyedListener) {
        this.shipDestroyedListener = shipDestroyedListener;
    }

}
