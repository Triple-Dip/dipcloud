package org.tripledip.diana.ui.startup;

import android.app.Fragment;

import org.tripledip.diana.service.GameService;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 *
 * This activity uses the GameService to connect to a server.  The user must choose which address
 * and port to listen on.
 *
 */
public class ClientConnectActivity extends ConnectActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_client;
    }

    @Override
    protected Fragment getFragment(GameService gameService) {
        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
        clientConnectionFragment.setGameService(gameService);
        return clientConnectionFragment;
    }

}
