package org.tripledip.diana.ui;

import android.app.Activity;

import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 */
public class ClientActivity extends ConnectActivity {

    @Override
    protected void attachFragments() {
        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.connect_frame, clientConnectionFragment)
                .commit();
    }
}
