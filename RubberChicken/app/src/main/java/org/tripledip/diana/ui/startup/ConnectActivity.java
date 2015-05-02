package org.tripledip.diana.ui.startup;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import org.tripledip.diana.service.GameService;
import org.tripledip.rubberchicken.R;

/**
 * Created by Ben on 4/27/15.
 *
 * Factor out connection behavior common to server and clients.
 *
 * Starts and binds to the GameService.  Once the binding is complete, attaches an appropriate
 * server or client connection fragment.
 *
 */
public abstract class ConnectActivity extends Activity {

    protected GameService gameService;

    protected abstract int getLayoutId();

    protected abstract Fragment getFragment(GameService gameService);

    protected void attachFragments() {
        Fragment fragment = getFragment(gameService);
        getFragmentManager().beginTransaction()
                .add(R.id.connect_frame, fragment)
                .commit();
    }

    private void detachFragments() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.connect_frame);
        if (null == fragment) {
            return;
        }
        getFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    protected void onPause() {
        detachFragments();

        if (null != gameService) {
            unbindService(serviceConnection);
            gameService = null;
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAndBindGameService();
    }

    private void startAndBindGameService() {
        // start the game service with a home activity to return to
        startService(GameService.makeStartIntent(this, this.getClass()));

        // bind the service and get callbacks at serviceConnection
        bindService(GameService.makeBindIntent(this), serviceConnection, 0);
    }

    // don't let user interact with fragments unless service is bound
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            gameService = ((GameService.GameServiceBinder) service).getService();
            attachFragments();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            detachFragments();
            gameService = null;
        }
    };

}
