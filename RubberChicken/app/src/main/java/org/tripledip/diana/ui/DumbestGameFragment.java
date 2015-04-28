package org.tripledip.diana.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.tripledip.diana.game.smashables.ComlinkMessage;
import org.tripledip.diana.game.GameEventListener;
import org.tripledip.diana.game.crew.CrewGameCore;
import org.tripledip.diana.game.smashables.Ship;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass. Created by Waffle
 */
public class DumbestGameFragment extends Fragment {

    CrewGameCore crewGameCore;

    public DumbestGameFragment() {
        // Required empty public constructor
        crewGameCore = new CrewGameCore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dumbest_game, container, false);

        //register listeners
        ShipStatusDisplay shipStatusDisplay = new ShipStatusDisplay(rootView);
        ComlinkDisplay comlinkDisplay = new ComlinkDisplay(rootView);

        return rootView;
    }

    public class ShipStatusDisplay implements GameEventListener<Ship>, View.OnClickListener{

        //status
        private TextView shipHpTextView;
        private Button damageShipButton;

        public ShipStatusDisplay(View rootView){

            //status
            shipHpTextView = (TextView) rootView.findViewById(R.id.shipHp);
            damageShipButton = (Button) rootView.findViewById(R.id.damageButton);
            damageShipButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            crewGameCore.bootStrapGame();
        }

        @Override
        public void onEventOccurred(Ship thing) {
            setShipOnUiThread(thing);
        }

        private void setShipOnUiThread (final Ship ship) {

            final Runnable ShipUi = new Runnable() {
                @Override
                public void run() {
                    shipHpTextView.setText(String.valueOf(ship.getShipHp()));
                }
            };

            getActivity().runOnUiThread(ShipUi);

        }
    }


    public class ComlinkDisplay implements GameEventListener<ComlinkMessage>, View.OnClickListener{

        private ArrayAdapter<String> messageAdapter;
        private ListView messageList;
        private EditText messageInputEditText;
        private Button sendMessageButton;

        public ComlinkDisplay(View rootView){
            messageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            messageList = (ListView) rootView.findViewById(R.id.message_list_view);
            messageList.setAdapter(messageAdapter);
            messageInputEditText = (EditText) rootView.findViewById(R.id.messageInput);
            sendMessageButton = (Button) rootView.findViewById(R.id.messageButton);
            sendMessageButton.setOnClickListener(this);
        }

        @Override
        public void onEventOccurred(ComlinkMessage thing) {
            setComlinkOnUiThread(thing);
        }

        @Override
        public void onClick(View v) {
        }

        private void addMessage(String message) {
            messageAdapter.add(message);
        }

        private void setComlinkOnUiThread (final ComlinkMessage comlinkMessage) {

            final Runnable ComlinkUi = new Runnable() {
                @Override
                public void run() {
                }
            };

            getActivity().runOnUiThread(ComlinkUi);

        }
    }



    public void setGameCoreDipAccess(DipAccess dipAccess){
        crewGameCore.setDipAccess(dipAccess);
    }



}
