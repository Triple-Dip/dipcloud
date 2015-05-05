package org.tripledip.diana.ui.game.crew;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tripledip.diana.game.GameEventNotifier;
import org.tripledip.diana.game.crew.ComlinkHelper;
import org.tripledip.diana.game.smashables.ComlinkMessage;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;


public class ComlinkGameFragment extends GameFragment<ComlinkMessage> {

    public ComlinkGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comlink, container, false);
    }

    private void drawMessage(final ComlinkMessage comlinkMessage) {

        final Runnable updateUi = new Runnable() {
            @Override
            public void run() {

            }
        };

        getActivity().runOnUiThread(updateUi);
    }


    @Override
    public void registerGameEventListeners() {
        ComlinkHelper comlinkHelper = (ComlinkHelper)
                gameCore.getHelpers().get(ComlinkHelper.class.getName());

        GameEventNotifier notifier = comlinkHelper.getGameEventNotifier();
        notifier.registerListener(ComlinkHelper.EVENT_COMLINK_MESSAGE_ARRIVED, this);

    }

    @Override
    public void unRegisterGameEventListeners() {
        ComlinkHelper comlinkHelper = (ComlinkHelper)
                gameCore.getHelpers().get(ComlinkHelper.class.getName());

        GameEventNotifier notifier = comlinkHelper.getGameEventNotifier();
        notifier.unRegisterListener(ComlinkHelper.EVENT_COMLINK_MESSAGE_ARRIVED, this);

    }

}
