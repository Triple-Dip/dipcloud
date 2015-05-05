package org.tripledip.diana.ui.startup;

import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 * <p/>
 * This activity uses the GameService to connect to a server.  The user must choose which address
 * and port to listen on.
 */
public class ClientConnectionActivity extends GameActivity {

    private ClientConnectionFragment clientConnectionFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_client;
    }

    @Override
    protected void findOrAttachFragments() {
        clientConnectionFragment = (ClientConnectionFragment)
                getFragmentManager().findFragmentById(R.id.connect_frame);

        if (null != clientConnectionFragment) {
            return;
        }

        clientConnectionFragment = ClientConnectionFragment.newInstance();
        clientConnectionFragment.setGameService(gameService);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.connect_frame, clientConnectionFragment)
                .commit();
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

}
