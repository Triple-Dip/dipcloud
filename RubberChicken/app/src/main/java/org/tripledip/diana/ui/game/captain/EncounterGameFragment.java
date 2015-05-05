package org.tripledip.diana.ui.game.captain;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class EncounterGameFragment extends GameFragment<Challenge> {


    @Override
    public void registerGameEventListeners() {

    }

    @Override
    public void unRegisterGameEventListeners() {

    }

    public EncounterGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_encounter_game, container, false);
    }


}
