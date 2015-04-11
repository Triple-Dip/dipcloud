package org.tripledip.landemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.demo.DemoFragment;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.behavior.DipServer;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 */
public class ServerActivity extends Activity {

    private DipServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_demo);

        // create a dip cloud
        createDip();
        addBootstrapAtoms();

        // create a ui
        if (savedInstanceState == null) {
            attachFragments();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDip();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDip();
    }

    private void createDip() {
        server = new DipServer(new Nimbase());
    }

    private void startDip() {
        server.startSessions();
    }

    private void stopDip() {
        server.stopSessions();
    }

    private void addBootstrapAtoms() {
        final Molecule bootstrap = new Molecule(DemoFragment.COLOUR_CHANNEL,
                new Atom(DemoFragment.LEFT_COLOUR, 0, Color.DKGRAY),
                new Atom(DemoFragment.RIGHT_COLOUR, 0, Color.DKGRAY));
        server.proposeAdd(bootstrap);
    }

    private void attachFragments() {

        DemoFragment serverFragment = DemoFragment.newInstance("Server", Color.RED, server);

        getFragmentManager().beginTransaction()
                .add(R.id.game_frame, serverFragment)
                .commit();
    }
}
