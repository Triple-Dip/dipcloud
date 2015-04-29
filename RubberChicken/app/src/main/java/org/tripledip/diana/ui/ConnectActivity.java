package org.tripledip.diana.ui;

import android.app.Activity;
import android.os.Bundle;

import org.tripledip.diana.service.GameService;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/27/15.
 */
public abstract class ConnectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        if (null == savedInstanceState) {
            attachFragments();
        }

        // start the game service with a home activity to return to
        startService(GameService.makeIntent(this, this.getClass()));
    }

    protected abstract void attachFragments();

    protected abstract int getLayoutId();
}
