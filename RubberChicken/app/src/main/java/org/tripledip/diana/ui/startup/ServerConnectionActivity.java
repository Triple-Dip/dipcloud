package org.tripledip.diana.ui.startup;

import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 *
 * This activity uses the GameService to accept client connections.  The user can choose which port
 * to listen on.
 *
 * The user must decide when to start accepting connections, when to stop accepting and start the
 * game, and when to start over and accept connections again.
 *
 */
public class ServerConnectionActivity extends GameActivity {

    private ServerConnectionFragment serverConnectionFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_server;
    }

    @Override
    protected void findOrAttachFragments() {
        serverConnectionFragment = (ServerConnectionFragment)
                getFragmentManager().findFragmentById(R.id.connect_frame);

        if (null != serverConnectionFragment) {
            return;
        }

        serverConnectionFragment = ServerConnectionFragment.newInstance();
        serverConnectionFragment.setGameService(gameService);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.connect_frame, serverConnectionFragment)
                .commit();
    }

    @Override
    protected void registerListeners() {

    }

    @Override
    protected void unregisterListeners() {

    }

}
