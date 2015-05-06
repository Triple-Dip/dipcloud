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
import org.tripledip.diana.game.crew.ChallengeHelper;
import org.tripledip.diana.game.smashables.Challenge;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.diana.ui.game.minigames.MiniGameFragment;
import org.tripledip.rubberchicken.R;

import java.util.ArrayList;
import java.util.List;


public class ChallengesGameFragment extends GameFragment<Challenge> implements AdapterView.OnItemClickListener {

    public static final String CREW_CHALLENGES_FRAG_TAG = "challengeFrag";

    private MiniGameFragment currentMiniGame;

    private boolean isCaptainsMode;

    private ChallengeHelper challengeHelper;

    private ArrayAdapter<Challenge> challengeArrayAdapter;

    private ListView listView;

    public ChallengesGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        challengeHelper = gameCore.getChallengeHelper();

        isCaptainsMode = gameCore.isCaptainMode();

        View view = inflater.inflate(R.layout.fragment_challenges, container, false);

        listView = (ListView) view.findViewById(R.id.challengesListView);
        challengeArrayAdapter = new ChallengesAdapter(getActivity(), new ArrayList<Challenge>());
        listView.setAdapter(challengeArrayAdapter);
        listView.setOnItemClickListener(this);

        if(isCaptainsMode){
            listView.setEnabled(false);
        }

        return view;

    }

    @Override
    public void registerGameEventListeners() {

        GameEventNotifier notifier = gameCore.getChallengeHelper().getGameEventNotifier();

        notifier.registerListener(ChallengeHelper.EVENT_ADD_CHALLENGE, this);
        notifier.registerListener(ChallengeHelper.EVENT_FINISH_CHALLENGE, this);
        notifier.registerListener(ChallengeHelper.EVENT_REMOVE_CHALLENGE, this);
        notifier.registerListener(ChallengeHelper.EVENT_START_CHALLENGE, this);

    }

    @Override
    public void unRegisterGameEventListeners() {

        GameEventNotifier notifier = challengeHelper.getGameEventNotifier();

        notifier.unRegisterListener(ChallengeHelper.EVENT_ADD_CHALLENGE, this);
        notifier.unRegisterListener(ChallengeHelper.EVENT_FINISH_CHALLENGE, this);
        notifier.unRegisterListener(ChallengeHelper.EVENT_REMOVE_CHALLENGE, this);
        notifier.unRegisterListener(ChallengeHelper.EVENT_START_CHALLENGE, this);
    }

    @Override
    public void onEventOccurred(String event, Challenge challenge) {
        switch (event){

            case ChallengeHelper.EVENT_ADD_CHALLENGE:
                updateListView();
                break;
            case ChallengeHelper.EVENT_REMOVE_CHALLENGE:
                updateListView();
                break;
            case ChallengeHelper.EVENT_FINISH_CHALLENGE:
                if(challenge.getOwner().equals(gameCore.getPlayer().getName())) {
                    detachMiniGame();
                    lockChallengeListView(false);
                }
                break;
            case ChallengeHelper.EVENT_START_CHALLENGE:
                if(challenge.getOwner().equals(gameCore.getPlayer().getName())) {
                    attachMiniGame(challenge);
                    lockChallengeListView(true);
                }
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        challengeHelper.requestOwnership(challengeHelper.getChallenges().get(position));
    }

    public class ChallengesAdapter extends ArrayAdapter<Challenge> {

        private final List<Challenge> challenges;

        public ChallengesAdapter(Context context, List<Challenge> challenges) {
            super(context, R.layout.listview_element_challenge, challenges);
            this.challenges = challenges;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_element_challenge, parent, false);
            }

            TextView challengeName = (TextView) convertView.findViewById(R.id.challengeName);

            challengeName.setText(challenges.get(position).getName());

            return convertView;
        }
    }

    private void updateListView() {

        final Runnable updateUi = new Runnable() {
            @Override
            public void run() {
                challengeArrayAdapter.clear();
                challengeArrayAdapter.addAll(challengeHelper.getChallenges());
                challengeArrayAdapter.notifyDataSetChanged();
            }
        };
        getActivity().runOnUiThread(updateUi);

    }

    private void lockChallengeListView(final boolean locked) {

        final Runnable updateUi = new Runnable() {
            @Override
            public void run() {
                listView.setEnabled(!locked);
            }
        };

        getActivity().runOnUiThread(updateUi);

    }

    private void attachMiniGame(Challenge challenge){

        currentMiniGame =
                gameCore.getMiniGameController().getMiniGameFromChallenge(challenge.getType());

        currentMiniGame.setChallengeHelper(challengeHelper);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.crew_minigame_container, currentMiniGame)
                .commit();

    }

    private void detachMiniGame(){
        getFragmentManager()
                .beginTransaction()
                .remove(currentMiniGame)
                .commit();

    }

}
