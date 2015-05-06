package org.tripledip.diana.ui.game.minigames;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.diana.game.crew.ChallengeHelper;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiniGameFragment extends Fragment {

    ChallengeHelper challengeHelper;

    public MiniGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);

        return textView;
    }

    public void setChallengeHelper(ChallengeHelper challengeHelper) {
        this.challengeHelper = challengeHelper;
    }

}
