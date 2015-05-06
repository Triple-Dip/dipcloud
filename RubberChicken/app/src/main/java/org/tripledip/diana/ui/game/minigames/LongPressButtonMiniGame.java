package org.tripledip.diana.ui.game.minigames;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LongPressButtonMiniGame extends MiniGameFragment implements View.OnClickListener {


    private Button purple1;
    private Button purple2;
    private Button purple3;
    private Button purple4;
    private Button purple5;
    private Button purple6;

    public LongPressButtonMiniGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_long_press_button_mini_game, container, false);

        purple1 = (Button) view.findViewById(R.id.purple1);
        purple1.setOnClickListener(this);
        purple2 = (Button) view.findViewById(R.id.purple2);
        purple2.setOnClickListener(this);
        purple3 = (Button) view.findViewById(R.id.purple3);
        purple3.setOnClickListener(this);
        purple4 = (Button) view.findViewById(R.id.purple4);
        purple4.setOnClickListener(this);
        purple5 = (Button) view.findViewById(R.id.purple5);
        purple5.setOnClickListener(this);
        purple6 = (Button) view.findViewById(R.id.purple6);
        purple6.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == purple2.getId()){

            challengeHelper.reportResult(Challenge.RESULT_FAILED);

        } else if (v.getId() == purple5.getId()) {

            challengeHelper.reportResult(Challenge.RESULT_SUCCESS);

        }

    }
}
