package org.tripledip.landemo;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Ben on 4/8/15.
 */
public class SocketConnectorTask extends AsyncTask<Socket, Void, Socket> {

    private Listener listener;

    private SocketAddress remoteAddress;

    private int timeoutMillis;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    protected Socket doInBackground(Socket... params) {
        if (null == params || 0 == params.length) {
            return null;
        }

        final Socket socket = params[0];

        if (null == remoteAddress) {
            return null;
        }

        try {
            socket.connect(remoteAddress, timeoutMillis);
        } catch (IOException e) {
            return null;
        }

        return socket;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        if (null == listener) {
            return;
        }
        listener.onSocketConnected(socket);
    }

    public interface Listener {
        public void onSocketConnected(Socket socket);
    }
}
