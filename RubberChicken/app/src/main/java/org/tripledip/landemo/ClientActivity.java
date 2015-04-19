package org.tripledip.landemo;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.demo.DemoFragment;
import org.tripledip.diana.service.GameService;
import org.tripledip.diana.ui.DumbestGameFragment;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.network.behavior.DipClient;
import org.tripledip.dipcloud.network.util.SocketProtocConnector;
import org.tripledip.rubberchicken.R;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Ben on 4/8/15.
 */
public class ClientActivity extends Activity {

    private DipClient dipClient;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_demo);

        // create a ui
        if (savedInstanceState == null) {
            attachFragments();
        }

        // start the game service
        startService(GameService.makeIntent(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopClient();
    }

    private void attachFragments() {
        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.connection_frame, clientConnectionFragment)
                .commit();
    }

    public void startClient(Socket socket) {
        Random random = new Random();
        int clientColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        dipClient = new DipClient(new Nimbase(), new SocketProtocConnector(socket));

//        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
//        DemoFragment clientFragment = DemoFragment.newInstance("Client", clientColor, dipClient);

        DumbestGameFragment clientFragment = new DumbestGameFragment();
        clientFragment.setGameCoreDipAccess(dipClient);

        getFragmentManager().beginTransaction()
                .add(R.id.game_frame, clientFragment)
                .commit();

        dipClient.start();
    }

    public void stopClient() {
        if (null != dipClient) {
            dipClient.stop();
        }
        dipClient = null;

        if (null != socket) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket = null;

        Fragment clientFragment = getFragmentManager().findFragmentById(R.id.game_frame);
        if (null != clientFragment) {
            getFragmentManager().beginTransaction()
                    .remove(clientFragment)
                    .commit();
        }
    }

}
