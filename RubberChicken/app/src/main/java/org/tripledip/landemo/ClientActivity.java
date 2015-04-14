package org.tripledip.landemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.demo.DemoFragment;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.network.behavior.DipClient;
import org.tripledip.dipcloud.network.util.SocketProtocConnector;
import org.tripledip.rubberchicken.R;

import java.net.Socket;

/**
 * Created by Ben on 4/8/15.
 */
public class ClientActivity extends Activity {

    private DipClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_demo);

        // create a ui
        if (savedInstanceState == null) {
            attachFragments();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDip();
    }

    private void stopDip() {
        if (null == client) {
            return;
        }
        client.stop();
    }

    private void attachFragments() {
        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.connection_frame, clientConnectionFragment)
                .commit();
    }

    public void startClient(Socket socket) {
        client = new DipClient(new Nimbase(), new SocketProtocConnector(socket));

        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
        DemoFragment clientFragment = DemoFragment.newInstance("Client", Color.BLUE, client);

        getFragmentManager().beginTransaction()
                .add(R.id.game_frame, clientFragment)
                .commit();

        client.start();
    }
}
