package org.tripledip.diana.ui.game.crew;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.diana.game.GameEventNotifier;
import org.tripledip.diana.game.crew.ShipHelper;
import org.tripledip.diana.game.smashables.Ship;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;


public class ShipStatusGameFragment extends GameFragment<Ship> {

    public static final String CREW_SHIPSTATUS_FRAG_TAG = "shipStatusFrag";

    private TextView shipHpValue;
    private TextView shipShieldValue;
    private TextView shipEnergyValue;

    public ShipStatusGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ship_status, container, false);

        shipHpValue = (TextView) view.findViewById(R.id.shipHpValue);
        shipShieldValue = (TextView) view.findViewById(R.id.shipShieldValue);
        shipEnergyValue = (TextView) view.findViewById(R.id.shipEnergyValue);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onEventOccurred(String subject, Ship ship) {
        drawShip(ship);
    }

    private void drawShip(final Ship ship) {

        final Runnable updateUi = new Runnable() {
            @Override
            public void run() {
                shipHpValue.setText(Integer.toString(ship.getShipHp()));
                shipShieldValue.setText(Integer.toString(ship.getShipShield()));
                shipEnergyValue.setText(Integer.toString(ship.getShipEnergy()));
            }
        };

        getActivity().runOnUiThread(updateUi);
    }

    @Override
    public void registerGameEventListeners() {

        GameEventNotifier notifier = gameCore.getShipHelper().getGameEventNotifier();

        notifier.registerListener(ShipHelper.EVENT_REFRESH_SHIP, this);
        notifier.registerListener(ShipHelper.EVENT_DAMAGE_HP, this);
        notifier.registerListener(ShipHelper.EVENT_DAMAGE_SHIELD, this);
        notifier.registerListener(ShipHelper.EVENT_DEPLETE_ENERGY, this);
        notifier.registerListener(ShipHelper.EVENT_RECHARGE_ENERGY, this);
        notifier.registerListener(ShipHelper.EVENT_REPAIR_HP, this);
        notifier.registerListener(ShipHelper.EVENT_REPAIR_SHIELD, this);
    }

    @Override
    public void unRegisterGameEventListeners() {

        GameEventNotifier notifier = gameCore.getShipHelper().getGameEventNotifier();

        notifier.unRegisterListener(ShipHelper.EVENT_REFRESH_SHIP, this);
        notifier.unRegisterListener(ShipHelper.EVENT_DAMAGE_HP, this);
        notifier.unRegisterListener(ShipHelper.EVENT_DAMAGE_SHIELD, this);
        notifier.unRegisterListener(ShipHelper.EVENT_DEPLETE_ENERGY, this);
        notifier.unRegisterListener(ShipHelper.EVENT_RECHARGE_ENERGY, this);
        notifier.unRegisterListener(ShipHelper.EVENT_REPAIR_HP, this);
        notifier.unRegisterListener(ShipHelper.EVENT_REPAIR_SHIELD, this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // make sure the ship atoms are bootstrapped after the ui is ready to show them
        if (gameCore.isCaptainMode()) {
            gameCore.getShipHelper().bootstrapSmashables();
        }
    }
}
