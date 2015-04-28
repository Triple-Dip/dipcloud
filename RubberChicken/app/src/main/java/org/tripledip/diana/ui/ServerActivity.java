package org.tripledip.diana.ui;

import android.app.Activity;

import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 */
public class ServerActivity extends ConnectActivity {

    @Override
    protected void attachFragments() {
        ServerConnectionFragment serverConnectionFragment = ServerConnectionFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.connect_frame, serverConnectionFragment)
                .commit();
    }
}
