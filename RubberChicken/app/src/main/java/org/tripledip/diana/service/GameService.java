package org.tripledip.diana.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.tripledip.diana.game.AbstractGameCore;
import org.tripledip.diana.game.Player;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.network.behavior.DipClient;
import org.tripledip.dipcloud.network.behavior.DipServer;
import org.tripledip.dipcloud.network.util.SocketProtocConnector;
import org.tripledip.rubberchicken.R;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/27/15.
 * <p/>
 * Here is a service that we can use to hold game state and do computations off the UI thread.
 * <p/>
 * First, some overall flavor.  It's both started and bound.  Started because we want it to outlive
 * activities in the game, and the game should still run if the player needs to Google something.
 * And bound because the game needs to call methods and get responses from it.
 * <p/>
 * Since it's started, we need a way to stop it.  The user will have to press a "quit" button to
 * make this happen.  To make this easy, the service foregrounds itself and puts up a notification
 * that the user can use to return to the activity or stop the service.
 * <p/>
 * The service will hold important state like open sockets, a "game core" object, and a dip client
 * or server.  It will provide methods for managing the lifecycle of these things.
 * <p/>
 * The service will run on the UI thread by default.  For accepting and connecting sockets, it will
 * spawn AsyncTasks.  It will also spawn inbox and outbox threads through the dip.
 */
public class GameService extends Service {

    public static final int NOTIFICATION_ID = 42;

    public static final String HOME_ACTIVITY_KEY = "homeActivity";
    public static final String POISON_PILL_KEY = "poisonPill";

    private final IBinder binder = new GameServiceBinder();

    private SocketAcceptorTask socketAcceptorTask;
    private SocketConnectorTask socketConnectorTask;
    private final List<Socket> sockets = new ArrayList<>();

    private DipAccess dipAccess;

    private AbstractGameCore gameCore;

    private Player player;

    public GameService() {
    }

    public AbstractGameCore getGameCore() {
        return gameCore;
    }

    public void setGameCore(AbstractGameCore gameCore) {
        this.gameCore = gameCore;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public class GameServiceBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

    public DipAccess getDipAccess() {
        return dipAccess;
    }

    public static Intent makeStartIntent(Context context, Class<? extends Activity> homeActivity) {
        Intent intent = new Intent(context, GameService.class);
        intent.putExtra(HOME_ACTIVITY_KEY, homeActivity);
        return intent;
    }

    public static Intent makeStopIntent(Context context) {
        Intent intent = new Intent(context, GameService.class);
        intent.putExtra(POISON_PILL_KEY, true);
        return intent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // let the system restart the service as needed
        if (null == intent) {
            return super.onStartCommand(intent, flags, startId);
        }

        // special call from notification to kill the service with poison
        if (intent.hasExtra(POISON_PILL_KEY)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        // normal call to start service from our Activity and return to it from the notification
        if (intent.hasExtra(HOME_ACTIVITY_KEY)) {
            putUpForegroundNotification(
                    (Class<? extends Activity>) intent.getExtras().getSerializable(HOME_ACTIVITY_KEY));
        }
        return super.onStartCommand(intent, flags, startId);
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
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        reset();
        takeDownForegroundNotification();
        super.onDestroy();
    }

    public void setHomeActivity(Class<? extends Activity> homeActivity) {
        putUpForegroundNotification(homeActivity);
    }

    private void putUpForegroundNotification(Class<? extends Activity> homeActivity) {
        Log.i(GameService.class.getName(), "starting foreground with home activity: " + homeActivity.getName());

        // Activity to launch from the service foreground notification
        Intent homeIntent = new Intent(this, homeActivity);
        PendingIntent homePendingIntent = PendingIntent.getActivity(
                this,
                0,
                homeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent shutdownPendingIntent = PendingIntent.getService(
                this,
                0,
                makeStopIntent(this),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // the notification itself
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_return))
                .setContentIntent(homePendingIntent)
                .addAction(android.R.drawable.ic_delete,
                        getString(R.string.notification_stop),
                        shutdownPendingIntent);

        startForeground(NOTIFICATION_ID, builder.build());
    }

    private void takeDownForegroundNotification() {
        Log.i(GameService.class.getName(), "stopping foreground");
        stopForeground(true);
    }

    private void closeSockets() {
        Log.i(GameService.class.getName(), "closing sockets: " + sockets.size());
        for (Socket socket : sockets) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sockets.clear();
    }

    public void reset() {
        Log.i(GameService.class.getName(), "resetting");
        if (null != dipAccess) {
            dipAccess.stop();
        }
        stopAccepting();
        stopConnecting();
        closeSockets();
    }

    // Reset all and set up dip server listening accepting clients.  Notify for each client.
    public void makeDipServer(int port, final SocketListener listener) {
        reset();
        dipAccess = new DipServer(new Nimbase());
        startAccepting(port, listener);
    }

    // Stop accepting clients and fire up all the client sessions.
    public void activateDipServer() {
        stopAccepting();
        dipAccess.start();
    }

    // Reset and try to connect to a server.  Listener will be notified on success.
    public void makeDipClient(SocketAddress socketAddress, SocketListener listener) {
        reset();
        startConnecting(socketAddress, listener);
    }

    private void startAccepting(int port, final SocketListener listener) {
        stopAccepting();
        Log.i(GameService.class.getName(), "accepting clients on port: " + port);
        socketAcceptorTask = new SocketAcceptorTask();

        // wrap the socket listener so we can make sessions and keep track of open sockets
        socketAcceptorTask.setListener(new SocketListener() {
            @Override
            public void onSocketConnected(Socket socket) {
                sockets.add(socket);
                ((DipServer) dipAccess).addSession(new SocketProtocConnector(socket));
                listener.onSocketConnected(socket);
            }
        });

        socketAcceptorTask.execute(port);
    }

    private void stopAccepting() {
        if (null != socketAcceptorTask) {
            Log.i(GameService.class.getName(), "stop accepting clients");
            socketAcceptorTask.cancelAcceptor();
        }
        socketAcceptorTask = null;
    }

    public boolean isAccepting() {
        return null != socketAcceptorTask;
    }

    private void startConnecting(SocketAddress socketAddress, final SocketListener socketListener) {
        stopConnecting();
        Log.i(GameService.class.getName(), "connecting to server at address " + socketAddress.toString());

        socketConnectorTask = new SocketConnectorTask();
        socketConnectorTask.setListener(new SocketListener() {
            @Override
            public void onSocketConnected(Socket socket) {
                if (null != socket) {
                    sockets.add(socket);
                    dipAccess = new DipClient(new Nimbase(), new SocketProtocConnector(socket));
                }
                socketListener.onSocketConnected(socket);
            }
        });
        socketConnectorTask.execute(socketAddress);
    }

    private void stopConnecting() {
        if (null != socketConnectorTask) {
            Log.i(GameService.class.getName(), "stop connecting to server");
            socketConnectorTask.cancelConnector();
        }
        socketConnectorTask = null;
    }

    public boolean isConnecting() {
        return null != socketConnectorTask;
    }
}
