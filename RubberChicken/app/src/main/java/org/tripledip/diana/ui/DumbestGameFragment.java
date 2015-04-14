package org.tripledip.diana.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tripledip.diana.game.ComlinkMessage;
import org.tripledip.diana.game.GameEventListener;
import org.tripledip.diana.game.GameCore;
import org.tripledip.diana.game.Ship;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass. Created by Waffle
 */
public class DumbestGameFragment extends Fragment {

    GameCore gameCore;

    public DumbestGameFragment() {
        // Required empty public constructor
        gameCore = new GameCore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dumbest_game, container, false);

        //register listeners
        gameCore.setOnShipDamagedListener(new ShipStatusDisplay(rootView));
        gameCore.setOnMessageSentListener(new ComlinkDisplay(rootView));
        gameCore.bootStrapGame();


        return rootView;
    }

    public class ShipStatusDisplay implements GameEventListener<Ship>, View.OnClickListener{

        //status
        private TextView shipHpTextView;
        private TextView shipDestroyedTextView;
        private Button damageShipButton;

        public ShipStatusDisplay(View rootView){

            //status
            shipHpTextView = (TextView) rootView.findViewById(R.id.shipHp);
            shipDestroyedTextView = (TextView) rootView.findViewById(R.id.shipDestroyed);
            damageShipButton = (Button) rootView.findViewById(R.id.damageButton);
            damageShipButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onEventOccurred(Ship thing) {

        }
    }


    public class ComlinkDisplay implements GameEventListener<ComlinkMessage>, View.OnClickListener{


        private TextView messageOutputTextView;
        private EditText messageInputEditText;
        private Button sendMessageButton;

        public ComlinkDisplay(View rootView){

            messageInputEditText = (EditText) rootView.findViewById(R.id.messageInput);
            messageOutputTextView = (TextView) rootView.findViewById(R.id.messageOutput);
            sendMessageButton = (Button) rootView.findViewById(R.id.messageButton);
            sendMessageButton.setOnClickListener(this);
        }

        @Override
        public void onEventOccurred(ComlinkMessage thing) {
            setComlinkOnUiThread(thing.getMessage());
        }

        @Override
        public void onClick(View v) {
            gameCore.sendComlinkMessage(messageInputEditText.getText().toString());
        }


        private void setComlinkOnUiThread (final String message) {

            final Runnable comlinkUi = new Runnable() {
                @Override
                public void run() {
                    messageOutputTextView.setText(message);
                }
            };

            getActivity().runOnUiThread(comlinkUi);

        }
    }


    public void setGameCoreDipAccess(DipAccess dipAccess){
        gameCore.setDipAccess(dipAccess);
    }



}
