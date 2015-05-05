package org.tripledip.diana.ui.game;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import org.tripledip.diana.game.AbstractGameCore;
import org.tripledip.diana.service.GameService;

/**
 * Created by Ben on 4/27/15.
 *
 * Factor out behavior common to Activities in the Game.  Especially, dealing with the Activity
 * lifecycle.
 *  - bind and unbind the game service
 *  - register and unregister game / dip listeners
 *  - attach and detach fragments
 *
 */
public abstract class GameActivity extends Activity {

    protected GameService gameService;

    protected abstract int getLayoutId();

    protected abstract void findOrAttachFragments();

    protected abstract void makeGameCore();

    protected abstract void registerListeners();

    protected abstract void unregisterListeners();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        startGameService();
    }

    @Override
    protected void onStop() {
        unregisterListeners();
        unbindGameService();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindGameService();
    }

    protected void startGameService() {
        startService(GameService.makeStartIntent(this, this.getClass()));
    }

    protected void stopGameService() {
        stopService(GameService.makeStopIntent(this));
    }

    protected void bindGameService() {
        bindService(GameService.makeBindIntent(this), serviceConnection, 0);
    }

    protected void unbindGameService() {
        unbindService(serviceConnection);
        if (null != gameService) {
            gameService = null;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            gameService = ((GameService.GameServiceBinder) service).getService();
            gameService.setHomeActivity(GameActivity.this.getClass());
            makeGameCore();
            findOrAttachFragments();
            registerListeners();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            unregisterListeners();
            gameService = null;
        }
    };

}
