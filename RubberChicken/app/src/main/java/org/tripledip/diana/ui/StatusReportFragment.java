package org.tripledip.diana.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.rubberchicken.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusReportFragment extends Fragment {

    public StatusReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ship, container, false);
    }

    public void setShipHp(String hpValue) {
        TextView shipHpTextView = (TextView) getView().findViewById(R.id.shipHpValue);
        shipHpTextView.setText(hpValue);
    }

    public void setShipShield(String shieldValue) {
        TextView shipShieldTextView = (TextView) getView().findViewById(R.id.shipShieldValue);
        shipShieldTextView.setText(shieldValue);
    }

    public void setShipStatus(String statusValue) {
        TextView shipStatusTextView = (TextView) getView().findViewById(R.id.shipStatusValue);
        shipStatusTextView.setText(statusValue);
    }

}
