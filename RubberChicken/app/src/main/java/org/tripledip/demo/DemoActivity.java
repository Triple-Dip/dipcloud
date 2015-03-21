package org.tripledip.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.behavior.DipClient;
import org.tripledip.dipcloud.network.behavior.DipServer;
import org.tripledip.dipcloud.network.contract.util.InMemoryConnectorPair;
import org.tripledip.rubberchicken.R;

public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        DipServer server = new DipServer(new Nimbase());

        InMemoryConnectorPair<Molecule> aToServer = new InMemoryConnectorPair<>();
        DipClient clientA = new DipClient(new Nimbase(), aToServer.getASendToB());
        server.addClientSession(aToServer.getBSendToA());

        server.startClientSessions();
        clientA.start();

        final Molecule bootstrap = new Molecule(DemoFragment.COLOUR_CHANNEL,
                new Atom(DemoFragment.LEFT_COLOUR, 0, Color.DKGRAY),
                new Atom(DemoFragment.RIGHT_COLOUR, 0, Color.DKGRAY));
        server.proposeAdd(bootstrap);

        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.gridColours, DemoFragment.newInstance("Server", Color.BLUE, server))
                    .add(R.id.gridColours, DemoFragment.newInstance("Client A", Color.RED, clientA))
                    .commit();
        }
    }
}
