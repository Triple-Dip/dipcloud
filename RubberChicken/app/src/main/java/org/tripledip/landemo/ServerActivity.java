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

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/8/15.
 */
public class ServerActivity extends Activity {

    private DipServer dipServer;
    private List<Socket> sockets = new ArrayList<Socket>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_demo);

        createDip();

        if (savedInstanceState == null) {
            attachFragments();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSessions();
    }

    private void createDip() {
        dipServer = new DipServer(new Nimbase());
    }

    private void addBootstrapAtoms() {
        long sequenceNumber = dipServer.getNimbase().nextSequenceNumber();
        final Molecule bootstrap = new Molecule(DemoFragment.COLOUR_CHANNEL,
                new Atom(DemoFragment.LEFT_COLOUR, sequenceNumber, Color.DKGRAY),
                new Atom(DemoFragment.RIGHT_COLOUR, sequenceNumber, Color.DKGRAY));
        dipServer.proposeAdd(bootstrap);
    }

    private void attachFragments() {

        ServerConnectionFragment serverConnectionFragment = ServerConnectionFragment.newInstance();
        DemoFragment serverFragment = DemoFragment.newInstance("Server", Color.RED, dipServer);

        getFragmentManager().beginTransaction()
                .add(R.id.connection_frame, serverConnectionFragment)
                .add(R.id.game_frame, serverFragment)
                .commit();
    }

    public void addSession(Socket socket) {
        dipServer.addSession(new SocketProtocConnector(socket));
    }

    public void startSessions() {
        dipServer.startSessions();
        addBootstrapAtoms();
    }

    public void stopSessions() {
        dipServer.stopSessions();
        for (Socket socket : sockets) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sockets.clear();
        dipServer.removeSessions();

        addBootstrapAtoms();
    }
}
