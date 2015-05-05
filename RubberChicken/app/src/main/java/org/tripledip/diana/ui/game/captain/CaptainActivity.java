package org.tripledip.diana.ui.game.captain;

import android.os.Bundle;

import org.tripledip.diana.service.GameService;
import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.diana.ui.game.crew.ComlinkGameFragment;
import org.tripledip.diana.ui.game.crew.ShipStatusGameFragment;
import org.tripledip.rubberchicken.R;

public class CaptainActivity extends GameActivity {


    ShipStatusGameFragment shipStatusGameFragment;
    ComlinkGameFragment comlinkGameFragment;
    EncounterGameFragment encounterGameFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_captain;
    }

    @Override
    protected GameService.StateOfPlay getStateOfPlay() {
        return GameService.StateOfPlay.PLAYING;
    }

    @Override
    protected void findOrAttachFragments() {
        shipStatusGameFragment = (ShipStatusGameFragment)
                getFragmentManager().findFragmentByTag(ShipStatusGameFragment.CREW_SHIPSTATUS_FRAG_TAG);
        comlinkGameFragment = (ComlinkGameFragment)
                getFragmentManager().findFragmentByTag(ComlinkGameFragment.CREW_COMLINK_FRAG_TAG);
        encounterGameFragment = (EncounterGameFragment)
                getFragmentManager().findFragmentByTag(EncounterGameFragment.CREW_ENCOUNTER_FRAG_TAG);

        if (null != shipStatusGameFragment &&
                null != comlinkGameFragment &&
                null != encounterGameFragment) {
            return;
        }

        shipStatusGameFragment = new ShipStatusGameFragment();
        shipStatusGameFragment.setGameCore(gameService.getGameCore());

        comlinkGameFragment = new ComlinkGameFragment();
        comlinkGameFragment.setGameCore(gameService.getGameCore());

        encounterGameFragment = new EncounterGameFragment();
        encounterGameFragment.setGameCore(gameService.getGameCore());

        getFragmentManager()
                .beginTransaction()
                .add(R.id.captain_ship_status_container, shipStatusGameFragment, ShipStatusGameFragment.CREW_SHIPSTATUS_FRAG_TAG)
                .add(R.id.captain_comlink_container, comlinkGameFragment, ComlinkGameFragment.CREW_COMLINK_FRAG_TAG)
                .add(R.id.captain_encounter_container, encounterGameFragment, EncounterGameFragment.CREW_ENCOUNTER_FRAG_TAG)
                .commit();
    }

    @Override
    protected void makeGameCore() {
        gameService.makeGameCore();
    }

    @Override
    protected void registerListeners() {
        shipStatusGameFragment.registerGameEventListeners();
        comlinkGameFragment.registerGameEventListeners();
        encounterGameFragment.registerGameEventListeners();
    }

    @Override
    protected void unregisterListeners() {
        shipStatusGameFragment.registerGameEventListeners();
        comlinkGameFragment.registerGameEventListeners();
        encounterGameFragment.registerGameEventListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain);
    }

}
