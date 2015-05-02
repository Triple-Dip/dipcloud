package org.tripledip.diana.ui.startup;

import android.app.Fragment;

import org.tripledip.diana.service.GameService;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 *
 * This activity uses the GameService to accept client connections.  The user can choose which port
 * to listen on.
 *
 * The user must decide when to start accepting connections, when to stop accepting and start the
 * game, and when to start over and accept connections again.

 */
public class ServerConnectActivity extends ConnectActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_server;
    }

    @Override
    protected Fragment getFragment(GameService gameService) {
        ServerConnectionFragment serverConnectionFragment = ServerConnectionFragment.newInstance();
        serverConnectionFragment.setGameService(gameService);
        return serverConnectionFragment;
    }

}
