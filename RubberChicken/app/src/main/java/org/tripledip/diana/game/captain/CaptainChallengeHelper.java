package org.tripledip.diana.game.captain;

import org.tripledip.diana.game.AbstractHelper;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.dipcloud.local.contract.DipAccess;

/**
 * Created by Wolfe on 4/28/2015.
 */
public class CaptainChallengeHelper extends AbstractHelper<Challenge>{

    public CaptainChallengeHelper(DipAccess dipAccess) {
        super(dipAccess, new Challenge());
    }

}
