package org.tripledip.diana.ui.startup;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.tripledip.diana.game.Player;
import org.tripledip.diana.service.GameService;
import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.rubberchicken.R;

public class OpeningActivity extends GameActivity {

    private EditText playerNameText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opening;
    }

    @Override
    protected GameService.StateOfPlay getStateOfPlay() {
        return GameService.StateOfPlay.OPENING;
    }

    @Override
    protected void findOrAttachFragments() {
        playerNameText = (EditText) findViewById(R.id.player_name_text);

        final Button captainButton = (Button) findViewById(R.id.captain_button);
        captainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(ServerConnectionActivity.class);
            }
        });

        final Button crewButton = (Button) findViewById(R.id.crew_button);
        crewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(ClientConnectionActivity.class);
            }
        });
    }

    @Override
    protected void makeGameCore() {

    }

    @Override
    protected void registerListeners() {

    }

    @Override
    protected void unregisterListeners() {

    }

    private Player makePlayer() {
        String playerName = playerNameText.getText().toString().trim();
        if (playerName.isEmpty()) {
            playerNameText.setError(getString(R.string.text_name_required));
            return null;
        }
        playerNameText.setError(null);
        return new Player(playerName);
    }

    private void launchActivity(Class<? extends Activity> activity) {
        Player player = makePlayer();
        if (null == player) {
            return;
        }

        gameService.setPlayer(player);
        startActivity(new Intent(this, activity));
    }
}
