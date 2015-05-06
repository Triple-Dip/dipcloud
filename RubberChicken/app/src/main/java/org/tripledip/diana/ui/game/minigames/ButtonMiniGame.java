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


    private Button failButton;
    private Button succeedButton;

    public ButtonMiniGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_mini_game, container, false);

        failButton = (Button) view.findViewById(R.id.failButton);
        failButton.setOnClickListener(this);
        succeedButton = (Button) view.findViewById(R.id.succeedButton);
        succeedButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == failButton.getId()){

            challengeHelper.reportResult(Challenge.RESULT_FAILED);

        } else if (v.getId() == succeedButton.getId()) {

            challengeHelper.reportResult(Challenge.RESULT_SUCCESS);

        }

    }
}
