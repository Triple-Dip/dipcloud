package org.tripledip.landemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.demo.DemoFragment;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.behavior.DipServer;
import org.tripledip.dipcloud.network.util.SocketProtocConnector;
import org.tripledip.rubberchicken.R;

import java.net.Socket;

/**
 * Created by Ben on 4/8/15.
 */
public class ServerActivity extends Activity {

    private DipServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_demo);

        createDip();

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

    private void createDip() {
        server = new DipServer(new Nimbase());
    }

    private void stopDip() {
        server.stopSessions();
    }

    private void addBootstrapAtoms() {
        long sequenceNumber = server.getNimbase().nextSequenceNumber();
        final Molecule bootstrap = new Molecule(DemoFragment.COLOUR_CHANNEL,
                new Atom(DemoFragment.LEFT_COLOUR, sequenceNumber, Color.DKGRAY),
                new Atom(DemoFragment.RIGHT_COLOUR, sequenceNumber, Color.DKGRAY));
        server.proposeAdd(bootstrap);
    }

    private void attachFragments() {

        ServerConnectionFragment serverConnectionFragment = ServerConnectionFragment.newInstance();
        DemoFragment serverFragment = DemoFragment.newInstance("Server", Color.RED, server);

        getFragmentManager().beginTransaction()
                .add(R.id.connection_frame, serverConnectionFragment)
                .add(R.id.game_frame, serverFragment)
                .commit();
    }

    public void addSession(Socket socket) {
        server.addSession(new SocketProtocConnector(socket));
    }

    public void startSessions() {
        server.startSessions();
        addBootstrapAtoms();
    }
}
