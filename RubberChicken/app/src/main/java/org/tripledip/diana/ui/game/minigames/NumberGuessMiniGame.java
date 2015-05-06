package org.tripledip.diana.ui.game.minigames;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumberGuessMiniGame extends MiniGameFragment implements View.OnClickListener {


    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;


    public NumberGuessMiniGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_guess_mini_game, container, false);

        one = (Button) view.findViewById(R.id.one);
        one.setOnClickListener(this);
        two = (Button) view.findViewById(R.id.two);
        two.setOnClickListener(this);
        three = (Button) view.findViewById(R.id.three);
        three.setOnClickListener(this);
        four = (Button) view.findViewById(R.id.four);
        four.setOnClickListener(this);
        five = (Button) view.findViewById(R.id.five);
        five.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == two.getId()){

            challengeHelper.reportResult(Challenge.RESULT_FAILED);

        } else {

            challengeHelper.reportResult(Challenge.RESULT_SUCCESS);

        }

    }
}
