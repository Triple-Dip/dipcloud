package org.tripledip.diana.ui;

import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/8/15.
 */
public class ClientConnectActivity extends ConnectActivity {

    @Override
    protected void attachFragments() {
        ClientConnectionFragment clientConnectionFragment = ClientConnectionFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.connect_frame, clientConnectionFragment)
                .commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_client;
    }
}
