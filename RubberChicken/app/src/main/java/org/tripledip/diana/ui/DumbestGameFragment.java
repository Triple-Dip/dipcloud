package org.tripledip.diana.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tripledip.diana.game.EventUiListener;
import org.tripledip.diana.game.GameCore;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
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



        return rootView;
    }


    public class ShipStatusDisplay implements EventUiListener{

        private TextView shipHpTextView;
        private TextView shipDestroyedTextView;
        private Button damageShipButton;

        public ShipStatusDisplay(View container){

        }


        @Override
        public void onEventOccurred(Atom atom) {

        }
    }

    public class ComlinkDisplay implements EventUiListener{

        private TextView messageOutputTextView;
        private EditText messageInputEditText;

        public ComlinkDisplay(){

        }


        @Override
        public void onEventOccurred(Atom atom) {

        }
    }


}
