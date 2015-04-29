package org.tripledip.diana.ui.game.crew;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tripledip.diana.game.smashables.Ship;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShipStatusGameFragment extends GameFragment<Ship> {


    public ShipStatusGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ship_status, container, false);
    }


}
