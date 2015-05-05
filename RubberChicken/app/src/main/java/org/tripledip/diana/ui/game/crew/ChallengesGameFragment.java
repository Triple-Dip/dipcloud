package org.tripledip.diana.ui.game.crew;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.tripledip.diana.game.GameEventNotifier;
import org.tripledip.diana.game.crew.CrewChallengeHelper;
import org.tripledip.diana.game.crew.CrewGameCore;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;

import java.util.List;


public class ChallengesGameFragment extends GameFragment<Challenge> implements AdapterView.OnItemClickListener{


    CrewChallengeHelper challengeHelper;

    public ChallengesGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        challengeHelper = (CrewChallengeHelper)
                gameCore.getHelpers().get(CrewChallengeHelper.class.getName());

        View view = inflater.inflate(R.layout.fragment_challenges, container, false);

        ListView listView = (ListView) view.findViewById(R.id.challengesListView);
        listView.setAdapter(new ChallengesAdapter(getActivity(), challengeHelper.getChallenges()));
        listView.setOnItemClickListener(this);

        return view;

    }

    @Override
    public void registerGameEventListeners() {

        GameEventNotifier notifier = challengeHelper.getGameEventNotifier();

        notifier.registerListener(CrewChallengeHelper.EVENT_ADD_CHALLENGE, this);
        notifier.registerListener(CrewChallengeHelper.EVENT_FINISH_CHALLENGE, this);
        notifier.registerListener(CrewChallengeHelper.EVENT_REMOVE_CHALLENGE, this);
        notifier.registerListener(CrewChallengeHelper.EVENT_START_CHALLENGE, this);
    }

    @Override
    public void unRegisterGameEventListeners() {

        GameEventNotifier notifier = challengeHelper.getGameEventNotifier();

        notifier.unRegisterListener(CrewChallengeHelper.EVENT_ADD_CHALLENGE, this);
        notifier.unRegisterListener(CrewChallengeHelper.EVENT_FINISH_CHALLENGE, this);
        notifier.unRegisterListener(CrewChallengeHelper.EVENT_REMOVE_CHALLENGE, this);
        notifier.unRegisterListener(CrewChallengeHelper.EVENT_START_CHALLENGE, this);
    }

    @Override
    public void onEventOccurred(String subject, Challenge thing) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        challengeHelper.removeChallenge(challengeHelper.getChallenges().get(position));
    }

    public class ChallengesAdapter extends ArrayAdapter<Challenge> {

        private final List<Challenge> challenges;

        public ChallengesAdapter(Context context, List<Challenge> challenges) {
            super(context, R.layout.listview_element_challenge, challenges);
            this.challenges = challenges;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getContext(), R.layout.listview_element_challenge, parent);

            TextView challengeName = (TextView) view.findViewById(R.id.challengeName);

            challengeName.setText(challenges.get(position).getName());

            return view;
        }
    }
}
