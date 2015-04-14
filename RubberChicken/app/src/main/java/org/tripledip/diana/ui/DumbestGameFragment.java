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
import org.tripledip.diana.game.EventUiListener;
import org.tripledip.diana.game.GameCore;
import org.tripledip.diana.game.Ship;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass. Created by Waffle
 */
public class DumbestGameFragment extends Fragment {

    GameCore gameCore;

    public DumbestGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dumbest_game, container, false);

        gameCore = new GameCore();
        //register listeners



        return rootView;
    }

    public class ShipStatusDisplay implements EventUiListener<Ship>, View.OnClickListener{

        private TextView shipHpTextView;
        private TextView shipDestroyedTextView;
        private Button damageShipButton;

        public ShipStatusDisplay(View rootView){

            shipHpTextView = (TextView) rootView.findViewById(R.id.shipHp);
            shipDestroyedTextView = (TextView) rootView.findViewById(R.id.shipDestroyed);
            damageShipButton = (Button) rootView.findViewById(R.id.damageButton);

        }




        // only called when "DAMAGE" button is pressed, passing in a -1 for now
        @Override
        public void onClick(View v) {

        }

        @Override
        public void onEventOccurred(Ship gameObject) {

        }
    }


    public class ComlinkDisplay implements EventUiListener<ComlinkMessage>{

        private TextView messageOutputTextView;
        private EditText messageInputEditText;
        private Button sendMessageButton;

        public ComlinkDisplay(View rootView){

            messageInputEditText = (EditText) rootView.findViewById(R.id.messageInput);
            messageOutputTextView = (TextView) rootView.findViewById(R.id.messageOutput);
            sendMessageButton = (Button) rootView.findViewById(R.id.messageButton);

        }


        @Override
        public void onEventOccurred(ComlinkMessage gameObject) {

        }
    }

    public void setGameCoreDipAccess(DipAccess dipAccess){
        gameCore.setDipAccess(dipAccess);
    }


}
