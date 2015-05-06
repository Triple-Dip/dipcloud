package org.tripledip.diana.game.crew;

import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.diana.ui.game.minigames.ButtonMiniGame;
import org.tripledip.diana.ui.game.minigames.LongPressButtonMiniGame;
import org.tripledip.diana.ui.game.minigames.MiniGameFragment;
import org.tripledip.diana.ui.game.minigames.NumberGuessMiniGame;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfe on 5/5/2015.
 */
public class MiniGameController {


    private final static Map<String, MiniGameFragment> challengeTypesToMiniGames = new HashMap<>();

    static{
        challengeTypesToMiniGames.put(Challenge.TYPE_BUTTON, new ButtonMiniGame());
        challengeTypesToMiniGames.put(Challenge.TYPE_LONG_PRESS, new LongPressButtonMiniGame());
        challengeTypesToMiniGames.put(Challenge.TYPE_NUMBER_GUESS, new NumberGuessMiniGame());
    }

    public MiniGameController(){

    }

    public MiniGameFragment getMiniGameFromChallenge(String challengeType) {
        return challengeTypesToMiniGames.get(challengeType);
    }

}
