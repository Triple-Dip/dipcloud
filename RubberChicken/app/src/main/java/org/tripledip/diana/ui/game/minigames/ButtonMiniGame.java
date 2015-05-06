package org.tripledip.diana.ui.game.minigames;


import android.content.DialogInterface;
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
public class ButtonMiniGame extends MiniGameFragment implements View.OnClickListener {


    private Button redButton;
    private Button blueButton;
    private Button greenButton;
    private Button yellowButton;

    public ButtonMiniGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_mini_game, container, false);

        redButton = (Button) view.findViewById(R.id.redButton);
        redButton.setOnClickListener(this);
        blueButton = (Button) view.findViewById(R.id.blueButton);
        blueButton.setOnClickListener(this);
        greenButton = (Button) view.findViewById(R.id.greenButton);
        greenButton.setOnClickListener(this);
        yellowButton = (Button) view.findViewById(R.id.yellowButton);
        yellowButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == redButton.getId()){

            challengeHelper.reportResult(Challenge.RESULT_FAILED);

        } else if (v.getId() == greenButton.getId()) {

            challengeHelper.reportResult(Challenge.RESULT_SUCCESS);

        }

    }
}
