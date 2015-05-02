package org.tripledip.diana.ui.demo;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;

import org.tripledip.diana.service.GameService;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.rubberchicken.R;

import java.util.Random;

/**
 * Created by Ben on 5/2/15.
 * <p/>
 * Binds to the GameService and hands it off to a demo fragment.
 */

public class DemoActivity extends Activity {

    private GameService gameService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    protected void onPause() {
        detachFragments();

        if (null != gameService) {
            gameService.getDipAccess().stop();
            unbindService(serviceConnection);
            gameService = null;
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindGameService();
    }

    private void bindGameService() {
        // bind the service and get callbacks at serviceConnection
        bindService(GameService.makeBindIntent(this), serviceConnection, 0);
    }

    // don't let user interact with fragments unless service is bound
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            gameService = ((GameService.GameServiceBinder) service).getService();
            attachFragments();
            gameService.getDipAccess().start();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            detachFragments();

            if (null != gameService) {
                gameService.getDipAccess().stop();
            }
            gameService = null;
        }
    };

    private int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    protected void attachFragments() {
        final String name = getString(R.string.app_name);
        final int color = randomColor();
        final DipAccess dipAccess = gameService.getDipAccess();

        Fragment fragment = ColorButtonFragment.newInstance(name, color, dipAccess);
        getFragmentManager().beginTransaction()
                .add(R.id.game_frame, fragment)
                .commit();
    }

    private void detachFragments() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.game_frame);
        if (null == fragment) {
            return;
        }
        getFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }
}
