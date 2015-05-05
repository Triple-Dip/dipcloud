package org.tripledip.diana.ui.game.crew;

import android.os.Bundle;

import org.tripledip.diana.game.captain.ComlinkController;
import org.tripledip.diana.game.captain.encounter.EncounterController;
import org.tripledip.diana.service.GameService;
import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.diana.ui.game.captain.EncounterGameFragment;
import org.tripledip.rubberchicken.R;

public class CrewActivity extends GameActivity {

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
                getFragmentManager().findFragmentByTag(ChallengesGameFragment.CREW_CHALLENGES_FRAG_TAG);
        shipStatusGameFragment = (ShipStatusGameFragment)
                getFragmentManager().findFragmentByTag(ShipStatusGameFragment.CREW_SHIPSTATUS_FRAG_TAG);
        comlinkGameFragment = (ComlinkGameFragment)
                getFragmentManager().findFragmentByTag(ComlinkGameFragment.CREW_COMLINK_FRAG_TAG);

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
                .add(R.id.crew_challenges_container, challengesGameFragment, ChallengesGameFragment.CREW_CHALLENGES_FRAG_TAG)
                .add(R.id.crew_ship_status_container, shipStatusGameFragment, ShipStatusGameFragment.CREW_SHIPSTATUS_FRAG_TAG)
                .add(R.id.crew_comlink_container, comlinkGameFragment, ComlinkGameFragment.CREW_COMLINK_FRAG_TAG)
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
