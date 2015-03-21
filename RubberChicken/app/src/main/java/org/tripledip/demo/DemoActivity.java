package org.tripledip.demo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.behavior.DipClient;
import org.tripledip.dipcloud.network.behavior.DipServer;
import org.tripledip.dipcloud.network.contract.util.InMemoryConnectorPair;
import org.tripledip.rubberchicken.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoActivity extends Activity {

    private final String serverName = "Server";

    private final int serverColor = Color.RED;

    private final List<String> clientNames = Arrays.asList(
            "Client A", "Client B", "Client C", "Client D", "Client E");

    private final List<Integer> clientColors = Arrays.asList(
            Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW);

    private final List<DipClient> clients = new ArrayList<>();

    private final List<InMemoryConnectorPair<Molecule>> connectorPairs = new ArrayList<>();

    private DipServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        // create a dip cloud
        createDips();
        addBootstrapAtoms();

        // create a ui
        if (savedInstanceState == null) {
            attachFragments();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // activate the dip cloud
        startDips();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // deactivate the dip cloud
        stopDips();
    }

    private void createDips() {
        server = new DipServer(new Nimbase());

        for (String name : clientNames) {
            InMemoryConnectorPair<Molecule> clientToServer = new InMemoryConnectorPair<>();
            DipClient client = new DipClient(new Nimbase(), clientToServer.getASendToB());
            server.addClientSession(clientToServer.getBSendToA());

            clients.add(client);
            connectorPairs.add(clientToServer);
        }
    }

    private void startDips() {
        server.startClientSessions();
        for (DipClient client : clients) {
            client.start();
        }
    }

    private void stopDips() {
        server.stopClientSessions();
        for (DipClient client : clients) {
            client.stop();
        }
    }

    private void addBootstrapAtoms() {
        final Molecule bootstrap = new Molecule(DemoFragment.COLOUR_CHANNEL,
                new Atom(DemoFragment.LEFT_COLOUR, 0, Color.DKGRAY),
                new Atom(DemoFragment.RIGHT_COLOUR, 0, Color.DKGRAY));
        server.proposeAdd(bootstrap);
    }

    private void attachFragments() {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        DemoFragment serverFragment = DemoFragment.newInstance(serverName, serverColor, server);
        fragmentTransaction.add(R.id.gridColours, serverFragment);

        for (int i=0; i<clients.size(); i++) {
            String clientName = clientNames.get(i);
            int clientColor = clientColors.get(i);
            DipClient client = clients.get(i);

            DemoFragment clientFragment = DemoFragment.newInstance(clientName, clientColor, client);
            fragmentTransaction.add(R.id.gridColours, clientFragment);
        }

        fragmentTransaction.commit();
    }
}
