package org.tripledip.diana.ui.game.crew;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.tripledip.diana.game.GameEventNotifier;
import org.tripledip.diana.game.crew.ComlinkHelper;
import org.tripledip.diana.game.smashables.ComlinkMessage;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;

import java.util.ArrayList;
import java.util.List;


public class ComlinkGameFragment extends GameFragment<ComlinkMessage> {

    public static final String CREW_COMLINK_FRAG_TAG = "comlinkFrag";

    ArrayAdapter<ComlinkMessage> comlinkMessageArrayAdapter;
    ListView listView;

    public ComlinkGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comlink, container, false);



        listView = (ListView) view.findViewById(R.id.comlinkListView);
        comlinkMessageArrayAdapter = new ComlinkArrayAdapter(getActivity(), new ArrayList<ComlinkMessage>());
        listView.setAdapter(comlinkMessageArrayAdapter);

        return view;
    }


    @Override
    public void registerGameEventListeners() {

        GameEventNotifier notifier = gameCore.getComlinkHelper().getGameEventNotifier();
        notifier.registerListener(ComlinkHelper.EVENT_COMLINK_MESSAGE_ARRIVED, this);

    }

    @Override
    public void unRegisterGameEventListeners() {

        GameEventNotifier notifier = gameCore.getComlinkHelper().getGameEventNotifier();
        notifier.unRegisterListener(ComlinkHelper.EVENT_COMLINK_MESSAGE_ARRIVED, this);

    }


    public class ComlinkArrayAdapter extends ArrayAdapter<ComlinkMessage> {

        List<ComlinkMessage> comlinkMessages;

        public ComlinkArrayAdapter(Context context, List<ComlinkMessage> comlinkMessages) {
            super(context, R.layout.listview_element_comlink, comlinkMessages);
            this.comlinkMessages = comlinkMessages;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_element_challenge, parent, false);
            }

            TextView challengeName = (TextView) convertView.findViewById(R.id.challengeName);

            ComlinkMessage message = comlinkMessages.get(position);

            challengeName.setText(message.getMessage());
            challengeName.setTextColor(message.getColor());

            return convertView;
        }
    }

    @Override
    public void onEventOccurred(String event, ComlinkMessage comlinkMessage) {

        // using a switch to be consistent with the other helpers
        switch (event) {

            case ComlinkHelper.EVENT_COMLINK_MESSAGE_ARRIVED:
                comlinkMessageArrayAdapter.add(comlinkMessage);
                break;
        }

    }
}
