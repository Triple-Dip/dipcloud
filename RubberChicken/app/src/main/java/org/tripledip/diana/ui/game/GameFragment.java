package org.tripledip.diana.ui.game;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.diana.game.GameCore;
import org.tripledip.diana.game.GameEventListener;
import org.tripledip.rubberchicken.R;



public abstract class GameFragment<T> extends Fragment implements GameEventListener<T>{

    protected GameCore gameCore;

    public abstract void registerGameEventListeners();

    public abstract void unRegisterGameEventListeners();

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.text_start_game);
        return textView;
    }

    @Override
    public void onEventOccurred(String subject, T thing) {

    }

    public void setGameCore(GameCore gameCore) {
        this.gameCore = gameCore;
    }


}
