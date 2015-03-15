package org.tripledip.game;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.behavior.SuperDip;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.rubberchicken.R;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShipFragment extends Fragment implements View.OnClickListener{

    SuperDip superDip = new SuperDip(new Nimbase());

    public ShipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ship, container, false);
    }

    @Override
    public void onResume() {
        superDip.getChannelListeners().registerListener(GameConstants.CHANNEL_SHIP_STATE, new ShipStateListener());

        Atom hpAtom = new Atom(GameConstants.SHIP_HP_VALUE, Calendar.getInstance().getTimeInMillis(),null,100,0);
        Atom shieldAtom = new Atom(GameConstants.SHIP_SHIELD_VALUE, Calendar.getInstance().getTimeInMillis(),null,0,99.9);
        Atom statusAtom = new Atom(GameConstants.SHIP_STATUS_VALUE, Calendar.getInstance().getTimeInMillis(),"Good",0,0);

        superDip.proposeAdd(new Molecule(GameConstants.CHANNEL_SHIP_STATE, hpAtom, shieldAtom, statusAtom));

        getView().findViewById(R.id.damageButton).setOnClickListener(this);
        super.onResume();
    }

    public void setShipHp(String hpValue){
        TextView shipHpTextView = (TextView) getView().findViewById(R.id.shipHpValue);
        shipHpTextView.setText(hpValue);
    }

    public void setShipShield(String shieldValue){
        TextView shipShieldTextView = (TextView) getView().findViewById(R.id.shipShieldValue);
        shipShieldTextView.setText(shieldValue);
    }

    public void setShipStatus(String statusValue){
        TextView shipStatusTextView = (TextView) getView().findViewById(R.id.shipStatusValue);
        shipStatusTextView.setText(statusValue);
    }

    public class ShipStateListener implements ScrudListener<Molecule> {

        @Override
        public void onAdded(Molecule thing) {
            setShipState(thing);
        }

        @Override
        public void onUpdated(Molecule thing) {
            setShipState(thing);
        }

        @Override
        public void onRemoved(Molecule thing) {

        }

        @Override
        public void onSent(Molecule thing) {

        }

        private void setShipState(Molecule thing){
            setShipHp(String.valueOf(thing.findById(GameConstants.SHIP_HP_VALUE).getIntData()));
            setShipShield(String.valueOf(thing.findById(GameConstants.SHIP_SHIELD_VALUE).getDoubleData()));
            setShipStatus(thing.findById(GameConstants.SHIP_STATUS_VALUE).getStringData());
        }

    }

    @Override
    public void onClick(View v) {
        Atom hpAtom = new Atom(GameConstants.SHIP_HP_VALUE, Calendar.getInstance().getTimeInMillis(),null,20,0);
        Atom shieldAtom = new Atom(GameConstants.SHIP_SHIELD_VALUE, Calendar.getInstance().getTimeInMillis(),null,0,72.2);
        Atom statusAtom = new Atom(GameConstants.SHIP_STATUS_VALUE, Calendar.getInstance().getTimeInMillis(),"Bad",0,0);

        superDip.proposeUpdate(new Molecule(GameConstants.CHANNEL_SHIP_STATE, hpAtom, shieldAtom, statusAtom));
    }

}
