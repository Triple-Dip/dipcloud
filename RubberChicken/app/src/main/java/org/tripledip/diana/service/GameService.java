package org.tripledip.diana.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import org.tripledip.dipcloud.local.contract.DipAccess;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/27/15.
 *
 * Here is a service that we can use to hold game state and do computations off the UI thread.
 *
 * First, some overall flavor.  It's both started and bound.  Started because we want it to outlive
 * activities in the game, and the game should still run if the player needs to Google something.
 * And bound because the game needs to call methods and get responses from it.
 *
 * Since it's a started, we need a way to close it.  The user will have to press a "quit" button to
 * make this happen.  To make this easy, the service foregrounds itself and puts up a notification
 * that the user can use to return to the activity.
 *
 * The service will hold important state like open sockets, a "game core" object, and a dip client
 * or server.  It provide methods for managing the lifecycle of these things.
 *
 * The service will run on the UI thread by default.  For accepting and connecting sockets, it will
 * spawn AsyncTasks.  It will also spawn inbox and outbox threads through the dip.
 *
 * The service should be able to run indefinitely and restart itself without leaking resources.  For
 * example, the player should be able to play multiple rounds of the game.  Here is a rough sequence
 * of events that the service should be able to manage and repeat without leaking or getting
 * confused:
 *  - accept/connect one or more sockets
 *  - make a dip client or server
 *  - make a game core
 *  - start up the dip
 *  - start the game
 *  - play the game
 *  - stop the game
 *  - stop the dip
 *  - close the sockets
 *
 *  The same service should be able to work for game clients or game servers.  These activities
 *  would be expected to call some same and some different methods on the service.
 *
 */
public class GameService extends Service {

    public static final int NOTIFICATION_ID = 42;
    public static final String NOTIFICATION_TITLE = "Diana";
    public static final String NOTIFICATION_TEXT = "Return to game.";
    public static final String NOTIFICATION_SHUTDOWN = "Stop";

    public static final String HOME_ACTIVITY_KEY = "homeActivity";
    public static final String POISON_PILL_KEY = "poisonPill";

    private final IBinder binder = new GameServiceBinder();

    private SocketAcceptorTask socketAcceptorTask;
    private SocketConnectorTask socketConnectorTask;
    private final List<Socket> sockets = new ArrayList<>();

    private DipAccess dipAccess;

    private Object game;

    public GameService() {
    }

    public class GameServiceBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

    public DipAccess getDipAccess() {
        return dipAccess;
    }

    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
    }

    public Object getGame() {
        return game;
    }

    public void setGame(Object game) {
        this.game = game;
    }

    public static Intent makeStartIntent(Context context, Class<? extends Activity> homeActivity) {
        Intent intent = new Intent(context, GameService.class);
        intent.putExtra(HOME_ACTIVITY_KEY, homeActivity);
        return intent;
    }

    public static Intent makeBindIntent(Context context) {
        Intent intent = new Intent(context, GameService.class);
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
        if (intent.hasExtra(POISON_PILL_KEY)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        if (intent.hasExtra(HOME_ACTIVITY_KEY)) {
            putUpForegroundNotification(
                    (Class<? extends Activity>) intent.getExtras().getSerializable(HOME_ACTIVITY_KEY));
        }
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
        Intent homeIntent = new Intent(this, homeActivity);
        PendingIntent homePendingIntent = PendingIntent.getActivity(
                this,
                0,
                homeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent shutdownIntent = new Intent(this, GameService.class);
        shutdownIntent.putExtra(POISON_PILL_KEY, true);
        PendingIntent shutdownPendingIntent = PendingIntent.getService(
                this,
                0,
                shutdownIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // the notification itself
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_TEXT)
                .setContentIntent(homePendingIntent)
                .addAction(android.R.drawable.ic_delete, NOTIFICATION_SHUTDOWN, shutdownPendingIntent);

        startForeground(NOTIFICATION_ID, builder.build());
    }

    private void takeDownForegroundNotification() {
        stopForeground(true);
    }

    public void closeSockets() {
        for (Socket socket : sockets) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sockets.clear();
    }

    private void startAccepting(int port, final SocketListener listener) {
        stopAccepting();
        socketAcceptorTask = new SocketAcceptorTask();

        // wrap the socket listener so we can keep track of open sockets
        socketAcceptorTask.setListener(new SocketListener() {
            @Override
            public void onSocketConnected(Socket socket) {
                sockets.add(socket);
                listener.onSocketConnected(socket);
            }
        });

        socketAcceptorTask.execute(port);
    }

    private void stopAccepting() {
        if (null != socketAcceptorTask) {
            socketAcceptorTask.cancelAcceptor();
        }
        socketAcceptorTask = null;
    }

    private void startConnection(SocketAddress socketAddress, final SocketListener socketListener) {
        stopConnection();

        socketConnectorTask = new SocketConnectorTask();
        socketConnectorTask.setListener(new SocketListener() {
            @Override
            public void onSocketConnected(Socket socket) {
                sockets.add(socket);
                socketListener.onSocketConnected(socket);
            }
        });
        socketConnectorTask.execute(socketAddress);
    }

    private void stopConnection() {
        if (null != socketConnectorTask) {
            socketConnectorTask.cancelConnector();
        }
        socketConnectorTask = null;
    }

}
