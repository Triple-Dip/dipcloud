package org.tripledip.diana.ui.game.captain;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tripledip.diana.game.captain.encounter.DumbestEncounter;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class EncounterGameFragment extends GameFragment<Challenge> implements View.OnClickListener{

    public static final String CREW_ENCOUNTER_FRAG_TAG = "encounterFrag";


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

        View view = inflater.inflate(R.layout.fragment_encounter_game, container, false);

        Button button = (Button) view.findViewById(R.id.dumbestEncounterButton);
        button.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        gameCore.getEncounterController().startEncounter(new DumbestEncounter());
    }
}
