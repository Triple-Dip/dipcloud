package org.tripledip.diana.ui.game.crew;

import android.os.Bundle;

import org.tripledip.diana.service.GameService;
import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.rubberchicken.R;

public class CrewActivity extends GameActivity {

    public static final String CREW_CHALLENGES_FRAG_TAG = "challengeFrag";
    public static final String CREW_SHIPSTATUS_FRAG_TAG = "shipStatusFrag";
    public static final String CREW_COMLINK_FRAG_TAG = "comlinkFrag";

    ChallengesGameFragment challengesGameFragment;
    ShipStatusGameFragment shipStatusGameFragment;
    ComlinkGameFragment comlinkGameFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crew;
    }

    @Override
    protected GameService.StateOfPlay getStateOfPlay() {
        return GameService.StateOfPlay.PLAYING;
    }

    @Override
    protected void findOrAttachFragments() {

        challengesGameFragment = (ChallengesGameFragment)
                getFragmentManager().findFragmentByTag(CREW_CHALLENGES_FRAG_TAG);
        shipStatusGameFragment = (ShipStatusGameFragment)
                getFragmentManager().findFragmentByTag(CREW_SHIPSTATUS_FRAG_TAG);
        comlinkGameFragment = (ComlinkGameFragment)
                getFragmentManager().findFragmentByTag(CREW_COMLINK_FRAG_TAG);

        if (null != challengesGameFragment &&
                null != shipStatusGameFragment &&
                null != comlinkGameFragment) {
            return;
        }

        challengesGameFragment = new ChallengesGameFragment();
        challengesGameFragment.setGameCore(gameService.getGameCore());

        shipStatusGameFragment = new ShipStatusGameFragment();
        shipStatusGameFragment.setGameCore(gameService.getGameCore());

        comlinkGameFragment = new ComlinkGameFragment();
        comlinkGameFragment.setGameCore(gameService.getGameCore());

        getFragmentManager()
                .beginTransaction()
                .add(R.id.crew_challenges_container, challengesGameFragment, CREW_CHALLENGES_FRAG_TAG)
                .add(R.id.crew_ship_status_container, shipStatusGameFragment, CREW_SHIPSTATUS_FRAG_TAG)
                .add(R.id.crew_comlink_container, comlinkGameFragment, CREW_COMLINK_FRAG_TAG)
                .commit();

    }

    @Override
    protected void makeGameCore() {
        gameService.makeGameCore();
    }

    @Override
    protected void registerListeners() {
        shipStatusGameFragment.registerGameEventListeners();
        challengesGameFragment.registerGameEventListeners();
        comlinkGameFragment.registerGameEventListeners();
    }

    @Override
    protected void unregisterListeners() {
        shipStatusGameFragment.unRegisterGameEventListeners();
        challengesGameFragment.unRegisterGameEventListeners();
        comlinkGameFragment.unRegisterGameEventListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
    }

}
