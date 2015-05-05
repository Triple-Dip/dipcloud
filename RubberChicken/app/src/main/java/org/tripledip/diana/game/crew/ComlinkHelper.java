package org.tripledip.diana.game.crew;

import org.tripledip.diana.game.AbstractHelper;
import org.tripledip.diana.game.Player;
import org.tripledip.diana.game.smashables.ComlinkMessage;
import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 5/4/2015.
 */
public class ComlinkHelper extends AbstractHelper<ComlinkMessage>{

    public static final String EVENT_COMLINK_MESSAGE_ARRIVED = "comlinkMessageArrivedEvent";

    public ComlinkHelper(DipAccess dipAccess) {
        super(dipAccess, new ComlinkMessage());
    }

    public void sendComlinkMessage (ComlinkMessage comlinkMessage){
        dipAccess.proposeSend(comlinkMessage);
    }

    private void comlinkMessageArrived(ComlinkMessage comlinkMessage){
        gameEventNotifier.notifyEventOccurred(EVENT_COMLINK_MESSAGE_ARRIVED, comlinkMessage);
    }

    @Override
    public void onSent(ComlinkMessage comlinkMessage) {
        comlinkMessageArrived(comlinkMessage);
    }
    
}
