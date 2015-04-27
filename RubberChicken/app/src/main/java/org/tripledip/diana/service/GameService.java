package org.tripledip.diana.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameService extends Service {

    public static final int NOTIFICATION_ID = 42;
    public static final String NOTIFICATION_TITLE = "Diana";
    public static final String NOTIFICATION_TEXT = "Return to game.";

    public static final String HOME_ACTIVITY_KEY = "home activity";

    private final IBinder binder = new LocalGameService();

    private final List<Socket> sockets = new ArrayList<>();

    public GameService() {
    }

    public static Intent makeIntent(Context context, Class<? extends Activity> homeActivity) {
        Intent intent = new Intent(context, GameService.class);
        intent.putExtra(HOME_ACTIVITY_KEY, homeActivity);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        putUpForegroundNotification((Class<? extends Activity>) intent.getExtras().getSerializable(HOME_ACTIVITY_KEY));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        takeDownForegroundNotification();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void putUpForegroundNotification(Class<? extends Activity> homeActivity) {
        // Activity to launch from the service foreground notification
        Intent resultIntent = new Intent(this, homeActivity);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // the notification itself
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_TEXT)
                .setContentIntent(resultPendingIntent);

        startForeground(NOTIFICATION_ID, builder.build());
    }

    private void takeDownForegroundNotification() {
        stopForeground(true);
    }

    public class LocalGameService extends Binder {
        LocalGameService getService() {
            return LocalGameService.this;
        }
    }

}
