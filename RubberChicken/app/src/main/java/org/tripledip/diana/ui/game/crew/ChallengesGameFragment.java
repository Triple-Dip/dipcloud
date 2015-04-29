package org.tripledip.diana.ui.game.crew;


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
public class ChallengesGameFragment extends GameFragment<Challenge> {


    public ChallengesGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenges, container, false);
    }


}
