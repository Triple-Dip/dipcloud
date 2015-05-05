package org.tripledip.diana.game.captain;

import android.graphics.Color;

import org.tripledip.diana.game.GameEventListener;
import org.tripledip.diana.game.GameEventNotifier;
import org.tripledip.diana.game.crew.ChallengeHelper;
import org.tripledip.diana.game.GameCore;
import org.tripledip.diana.game.crew.ShipHelper;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.diana.game.smashables.ComlinkMessage;
import org.tripledip.diana.game.smashables.Ship;
import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 5/4/2015.
 */
public class ComlinkController {

    public static final int COMLINK_RED = Color.RED;
    public static final int COMLINK_BLUE = Color.BLUE;
    public static final int COMLINK_GREEN = Color.GREEN;

    private final GameCore gameCore;
    private final ShipGameEventListener shipGameEventListener;
    private final ChallengeGameEventListener challengeGameEventListener;
    private final DipAccess dipAccess;

    public ComlinkController(GameCore gameCore){
        this.gameCore = gameCore;
        this.dipAccess = gameCore.getDipAccess();

        shipGameEventListener = new ShipGameEventListener();
        challengeGameEventListener = new ChallengeGameEventListener();

        // wire up listeners
        GameEventNotifier notifier;
        notifier = gameCore.getShipHelper().getGameEventNotifier();
        notifier.registerListener(ShipHelper.EVENT_DAMAGE_HP, shipGameEventListener);
        notifier.registerListener(ShipHelper.EVENT_DAMAGE_SHIELD, shipGameEventListener);
        notifier.registerListener(ShipHelper.EVENT_DEPLETE_ENERGY, shipGameEventListener);
        notifier.registerListener(ShipHelper.EVENT_RECHARGE_ENERGY, shipGameEventListener);
        notifier.registerListener(ShipHelper.EVENT_REPAIR_HP, shipGameEventListener);
        notifier.registerListener(ShipHelper.EVENT_REPAIR_SHIELD, shipGameEventListener);

        notifier = gameCore.getChallengeHelper().getGameEventNotifier();
        notifier.registerListener(ChallengeHelper.EVENT_ADD_CHALLENGE, challengeGameEventListener);
        notifier.registerListener(ChallengeHelper.EVENT_REMOVE_CHALLENGE, challengeGameEventListener);
        notifier.registerListener(ChallengeHelper.EVENT_FINISH_CHALLENGE, challengeGameEventListener);
        notifier.registerListener(ChallengeHelper.EVENT_START_CHALLENGE, challengeGameEventListener);

    }



    public class ShipGameEventListener implements GameEventListener<Ship> {

        @Override
        public void onEventOccurred(String event, Ship thing) {

            switch (event){

                case ShipHelper.EVENT_DAMAGE_HP: dipAccess.proposeSend(new ComlinkMessage("Ship has been damaged!",COMLINK_RED));
                    break;
                case ShipHelper.EVENT_REPAIR_HP: dipAccess.proposeSend(new ComlinkMessage("Ship has been repaired!",COMLINK_GREEN));
                    break;

            }

        }

    }

    public class ChallengeGameEventListener implements GameEventListener<Challenge>{

        @Override
        public void onEventOccurred(String event, Challenge thing) {

            switch (event){
                case ChallengeHelper.EVENT_START_CHALLENGE:
                    dipAccess.proposeSend(new ComlinkMessage(thing.getOwner()+" has started "+thing.getName()+"!", COMLINK_BLUE));
                    break;
                case ChallengeHelper.EVENT_FINISH_CHALLENGE:
                    dipAccess.proposeSend(new ComlinkMessage(thing.getOwner()+" has "+thing.getResult()+" "+thing.getName()+"!", COMLINK_BLUE));
                    break;
                case ChallengeHelper.EVENT_REMOVE_CHALLENGE:
                    dipAccess.proposeSend(new ComlinkMessage(thing.getOwner()+" has removed "+thing.getName()+"!", COMLINK_BLUE));
                    break;

            }

        }
    }



}
